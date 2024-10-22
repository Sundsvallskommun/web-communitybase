package se.dosf.communitybase.modules.addsection;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.beans.AddSectionType;
import se.dosf.communitybase.beans.NewSectionValues;
import se.dosf.communitybase.enums.DeleteDateMode;
import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.SectionType;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleSectionDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.crud.MultipartLimitProvider;
import se.unlogic.hierarchy.core.utils.crud.MultipartRequestFilter;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class AddSectionModule extends AnnotatedForegroundModule implements MultipartLimitProvider {

	private static final AnnotatedRequestPopulator<NewSectionValues> POPULATOR = new AnnotatedRequestPopulator<NewSectionValues>(NewSectionValues.class);

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max upload size", description = "Maxmium upload size in megabytes allowed in a single post request", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Integer maxRequestSize = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Integer ramThreshold = 500;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Section image max size (in px)", description = "The max size of the section image (default is 270)", required = true, formatValidator = StringIntegerValidator.class)
	protected Integer sectionImageSize = 270;

	@InstanceManagerDependency(required = true)
	protected CBInterface cbInterface;

	protected MultipartRequestFilter multipartFiler = new MultipartRequestFilter(this);
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(AddSectionModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + AddSectionModule.class.getName());
		}
	}
	
	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(AddSectionModule.class, this);

		super.unload();
	}

	@Override
	protected ForegroundModuleResponse processForegroundRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		List<SectionType> sectionTypes = cbInterface.getAvailableSectionTypes(user);

		if (sectionTypes == null) {

			Document doc = createDocument(req, uriParser);
			doc.getDocumentElement().appendChild(doc.createElement("NoSectionTypesAvailable"));

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
		}

		ValidationException validationException = null;

		if (req.getMethod().equals("POST")) {

			try {
				req = multipartFiler.parseRequest(req, user);

				NewSectionValues newSectionValues = POPULATOR.populate(req);

				checkSectionType(newSectionValues, sectionTypes, user);

				newSectionValues.setLogo(getLogo(req));

				log.info("User " + user + " adding section " + newSectionValues.getName());

				SectionInterface sectionInterface = cbInterface.addSection(newSectionValues, user);

				log.info("User " + user + " added section " + sectionInterface.getSectionDescriptor());

				res.sendRedirect(req.getContextPath() + sectionInterface.getSectionDescriptor().getFullAlias());

				return null;

			} catch (ValidationException e) {

				validationException = e;

			} finally {

				multipartFiler.releaseRequest(req, user);
			}
		}

		log.info("User requested add section form");

		Document doc = createDocument(req, uriParser);
		Element documentElement = doc.getDocumentElement();

		Element addSectionElement = doc.createElement("AddSection");
		documentElement.appendChild(addSectionElement);

		XMLUtils.appendNewElement(doc, addSectionElement, "MaxAllowedFileSize", BinarySizeFormater.getFormatedSize(maxRequestSize * BinarySizes.MegaByte));

		Element sectionTypesElement = XMLUtils.appendNewElement(doc, addSectionElement, "SectionTypes");
		
		if (!CollectionUtils.isEmpty(sectionTypes)) {

			for(SectionType sectionType : sectionTypes){
				
				Element sectionTypeElement = (Element) sectionTypesElement.appendChild(sectionType.toXML(doc));
			
				Set<SectionAccessMode> sectionAccessModes = new HashSet<SectionAccessMode>();
	
				if (!CollectionUtils.isEmpty(sectionType.getAddSectionTypes())) {
	
					for (AddSectionType addSectionType : sectionType.getAddSectionTypes()) {
	
						for (Group group : user.getGroups()) {
	
							if (addSectionType.getGroupID().equals(group.getGroupID())) {
	
								sectionAccessModes.addAll(addSectionType.getAccessModes());
							}
						}
					}
	
				} else {
	
					sectionAccessModes.add(SectionAccessMode.OPEN);
					sectionAccessModes.add(SectionAccessMode.CLOSED);
					sectionAccessModes.add(SectionAccessMode.HIDDEN);
				}
	
				Element accessModesElement = XMLUtils.appendNewElement(doc, sectionTypeElement, "AccessModes");
	
				for (SectionAccessMode sectionAccessMode : sectionAccessModes) {
	
					XMLUtils.appendNewElement(doc, accessModesElement, "AccessMode", sectionAccessMode.name());
				}
			}
			
		}

		if (validationException != null) {

			addSectionElement.appendChild(validationException.toXML(doc));
			addSectionElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());

		setLinksAndScripts(moduleResponse);

		return moduleResponse;
	}

	private BufferedImage getLogo(HttpServletRequest req) throws ValidationException {

		if (!(req instanceof MultipartRequest)) {

			return null;
		}

		MultipartRequest multipartRequest = (MultipartRequest) req;

		if (multipartRequest.getFileCount() > 0 && !StringUtils.isEmpty(multipartRequest.getFile(0).getName())) {

			FileItem file = multipartRequest.getFile(0);

			String lowerCasefileName = file.getName().toLowerCase();

			if (!(lowerCasefileName.endsWith(".png") || lowerCasefileName.endsWith(".jpg") || lowerCasefileName.endsWith(".gif") || lowerCasefileName.endsWith(".bmp"))) {

				throw new ValidationException(new ValidationError("InvalidProfileImageFileFormat"));

			} else {

				try {

					BufferedImage image = ImageUtils.getImage(file.get());

					image = ImageUtils.cropAsSquare(image);

					if (image.getWidth() > sectionImageSize || image.getHeight() > sectionImageSize) {

						image = ImageUtils.scale(image, sectionImageSize, sectionImageSize, Image.SCALE_SMOOTH, BufferedImage.TYPE_INT_ARGB);

					}

					return image;

				} catch (Exception e) {

					throw new ValidationException(new ValidationError("UnableToParseLogoImage"));
				}

			}
		}

		return null;
	}

	private void checkSectionType(NewSectionValues newSectionValues, List<SectionType> addSectionTypes, User user) throws ValidationException {

		for (SectionType sectionType : addSectionTypes) {

			if (sectionType.getSectionTypeID().equals(newSectionValues.getSectionTypeID())) {

				if (!CollectionUtils.isEmpty(sectionType.getAddSectionTypes())) {

					boolean validSectionAccess = false;

					outerLoop: for (AddSectionType addSectionType : sectionType.getAddSectionTypes()) {

						for (Group group : user.getGroups()) {

							if (addSectionType.getGroupID().equals(group.getGroupID())) {

								if (addSectionType.getAccessModes().contains(newSectionValues.getAccessMode())) {

									validSectionAccess = true;
									break outerLoop;
								}
							}
						}
					}

					if (!validSectionAccess) {

						throw new ValidationException(new ValidationError("InvalidSectionAccess"));
					}
				}
				
				if (sectionType.getDeleteDateMode().equals(DeleteDateMode.REQUIRED) && newSectionValues.getSectionDeleteDate() == null) {
					
					throw new ValidationException(new ValidationError("sectionDeleteDate", ValidationErrorType.RequiredField));
				}
				
				if (newSectionValues.getSectionDeleteDate() != null && newSectionValues.getSectionDeleteDate().before(DateUtils.getCurrentSQLDate(false))) {
					throw new ValidationException(new ValidationError("sectionDeleteDate", ValidationErrorType.InvalidFormat));
				}

				return;
			}
		}

		throw new ValidationException(new ValidationError("InvalidSectionType"));
	}

	protected Document createDocument(HttpServletRequest req, URIParser uriParser) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));

		return doc;
	}

	@Override
	public int getRamThreshold() {

		return ramThreshold;
	}

	@Override
	public long getMaxRequestSize() {

		return maxRequestSize;
	}

	@Override
	public String getTempDir() {

		return null;
	}

	public AccessInterface getModuleDescriptor() {

		return moduleDescriptor;
	}
}
