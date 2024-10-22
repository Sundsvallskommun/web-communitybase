ALTER TABLE `groups` ADD COLUMN `email` VARCHAR(255) AFTER `name`;
ALTER TABLE `invitations` ADD COLUMN `senderEmail` VARCHAR(255) AFTER `resend`;