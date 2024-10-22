
Test på baku-2 med sundsvall backup från 2013-12-07

                    Före - efter - borttagna
grupper i filearchive 640 -  630 =  10		SELECT * FROM filearchive_sectiongroups f;
skolor i filearchive  286 -  286 =   0		SELECT * FROM filearchive_sectionschools f;
grupper i gallery    2026 - 1918 = 108		SELECT * FROM picturegallery_gallerygroups p;
skolor i gallery      110 -  110 =   0		SELECT * FROM picturegallery_galleryschools p;

ogiltiga gallery grupper 11 - 0 = 11 		SELECT DISTINCT groupID FROM picturegallery_gallerygroups WHERE groupID NOT IN (SELECT groupID FROM groups);
bilder i gallery       ? - 41337 = ? 			SELECT COUNT(pictureID) FROM picturegallery_pictures p;
newsletters       ?11599 - 11008 =  591		SELECT COUNT(weekLetterID) FROM newsletters p;
kalender poster    15910 - 12622 = 1485 	SELECT COUNT(calendarID) FROM calendar p;
gallerier          ?2037 -  1930 =  107		SELECT COUNT(galleryID) FROM picturegallery_galleries p;
filesections        ?852 -   845 =    7		SELECT COUNT(sectionID) FROM filearchive_sections p;

? glömde kolla innan och db restore tar lång tid.


Det saknades foreign keys på picturegallery_gallerygroups.groupID, picturegallery_galleryschools.schoolID, filearchive_sectiongroups.groupID och filearchive_sectionschools.schoolID

### picturegallery

--- bilder som har varken grupp eller skola

gallerier utan grupp eller skola:
SELECT DISTINCT ga.galleryID FROM picturegallery_galleries ga LEFT OUTER JOIN picturegallery_gallerygroups g ON ga.galleryID = g.galleryID LEFT OUTER JOIN picturegallery_galleryschools s ON ga.galleryID = s.galleryID WHERE groupID IS null AND schoolID is null AND global = 0;

antal bilder som har varken grupp eller skola:
SELECT COUNT(pictureID) FROM picturegallery_pictures p INNER JOIN (SELECT DISTINCT ga.galleryID FROM picturegallery_galleries ga LEFT OUTER JOIN picturegallery_gallerygroups g ON ga.galleryID = g.galleryID LEFT OUTER JOIN picturegallery_galleryschools s ON ga.galleryID = s.galleryID WHERE groupID IS null AND schoolID is null AND global = 0) o ON p.galleryID = o.galleryID;

--- bilder som har ogiltig grupp eller skola

ogiltiga grupper:
SELECT DISTINCT groupID FROM picturegallery_gallerygroups WHERE groupID NOT IN (SELECT groupID FROM groups);

ogiltiga skolor:
SELECT DISTINCT schoolID FROM picturegallery_galleryschools WHERE schoolID NOT IN (SELECT schoolID FROM schools);

ta bort ogiltiga grupper:
DELETE FROM picturegallery_gallerygroups WHERE groupID NOT IN (SELECT groupID FROM groups);

ta bort ogiltiga skolor:
DELETE FROM picturegallery_galleryschools WHERE schoolID NOT IN (SELECT schoolID FROM schools);

gallerier med ogiltiga grupper eller skolor:
SELECT DISTINCT ga.galleryID FROM picturegallery_galleries ga LEFT OUTER JOIN picturegallery_gallerygroups g ON ga.galleryID = g.galleryID LEFT OUTER JOIN picturegallery_galleryschools s ON ga.galleryID = s.galleryID LEFT OUTER JOIN (SELECT DISTINCT groupID FROM picturegallery_gallerygroups WHERE groupID NOT IN (SELECT groupID FROM groups)) invG ON invG.groupID = g.groupID LEFT OUTER JOIN (SELECT DISTINCT schoolID FROM picturegallery_galleryschools WHERE schoolID NOT IN (SELECT schoolID FROM schools)) invS ON invS.schoolID = s.schoolID WHERE NOT invS.schoolID IS null OR NOT invG.groupID IS null;

antal bilder som har ogiltig grupp eller skola:
SELECT COUNT(pictureID) FROM picturegallery_pictures p INNER JOIN (SELECT DISTINCT ga.galleryID FROM picturegallery_galleries ga LEFT OUTER JOIN picturegallery_gallerygroups g ON ga.galleryID = g.galleryID LEFT OUTER JOIN picturegallery_galleryschools s ON ga.galleryID = s.galleryID LEFT OUTER JOIN (SELECT DISTINCT groupID FROM picturegallery_gallerygroups WHERE groupID NOT IN (SELECT groupID FROM groups)) invG ON invG.groupID = g.groupID LEFT OUTER JOIN (SELECT DISTINCT schoolID FROM picturegallery_galleryschools WHERE schoolID NOT IN (SELECT schoolID FROM schools)) invS ON invS.schoolID = s.schoolID WHERE NOT invS.schoolID IS null OR NOT invG.groupID IS null) o ON p.galleryID = o.galleryID;

### filer

--- fil sektioner utan grupp eller skola:
SELECT DISTINCT fs.sectionID FROM filearchive_sections fs LEFT OUTER JOIN filearchive_sectiongroups g ON fs.sectionID = g.sectionID LEFT OUTER JOIN filearchive_sectionschools s ON fs.sectionID = s.sectionID WHERE groupID IS null AND schoolID is null AND global = 0;

--- fil sektioner som har ogiltig grupp eller skola

ogiltiga grupper:
SELECT DISTINCT groupID FROM filearchive_sectiongroups WHERE groupID NOT IN (SELECT groupID FROM groups);

ogiltiga skolor:
SELECT DISTINCT schoolID FROM filearchive_sectionschools WHERE schoolID NOT IN (SELECT schoolID FROM schools);

ta bort ogiltiga grupper:
DELETE FROM filearchive_sectiongroups WHERE groupID NOT IN (SELECT groupID FROM groups);

ta bort ogiltiga skolor:
DELETE FROM filearchive_sectionschools WHERE schoolID NOT IN (SELECT schoolID FROM schools);

fil sektioner med ogiltiga grupper eller skolor:
SELECT DISTINCT fs.sectionID FROM filearchive_sections fs LEFT OUTER JOIN filearchive_sectiongroups g ON fs.sectionID = g.sectionID LEFT OUTER JOIN filearchive_sectionschools s ON fs.sectionID = s.sectionID LEFT OUTER JOIN (SELECT DISTINCT groupID FROM filearchive_sectiongroups WHERE groupID NOT IN (SELECT groupID FROM groups)) invG ON invG.groupID = g.groupID LEFT OUTER JOIN (SELECT DISTINCT schoolID FROM filearchive_sectionschools WHERE schoolID NOT IN (SELECT schoolID FROM schools)) invS ON invS.schoolID = s.schoolID WHERE NOT invS.schoolID IS null OR NOT invG.groupID IS null;

### nyhetsbrev

--- nyhetsbrev som har varken grupp eller skola:
SELECT DISTINCT n.weekLetterID FROM newsletters n LEFT OUTER JOIN groupnewsletters g ON n.weekLetterID = g.weekLetterID LEFT OUTER JOIN schoolnewsletters s ON n.weekLetterID = s.weekLetterID WHERE groupID IS null AND schoolID IS null AND global = 0;

### kalender

--- kalendrar som har varken grupp eller skola eller global:
SELECT DISTINCT c.calendarID FROM calendar c LEFT OUTER JOIN groupcalendar g ON c.calendarID = g.calendarID LEFT OUTER JOIN schoolcalendar s ON c.calendarID = s.calendarID LEFT OUTER JOIN globalcalendar gc ON gc.calendarID = c.calendarID WHERE groupID IS null AND schoolID IS null AND gc.calendarID IS null;

