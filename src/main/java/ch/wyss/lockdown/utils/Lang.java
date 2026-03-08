package ch.wyss.lockdown.utils;

import org.bukkit.entity.Player;

public enum Lang {
    // keep all the strings translation in here
    PLAYER_NOT_OP("Du bist kein Operator", "You are not an operator"),
    NO_ACTION_PROVIDED("Bitte gebe eine Aktion an", "Please provide an action"),
    NO_VALID_ACTION("Bitte gebe eine gültige Aktion an", "Please provide a valid action"),
    YOU_ARE_ALREADY_WHITELISTED("Du bist bereits auf der Whitelist was willst du mehr?", "You are already on the whitelist what more do you want?"),
    PASSWORD_PROTECTION_ENABLED("Passwortschutz aktiviert", "Password protection enabled"),
    PASSWORD_PROTECTION_DISABLED("Passwortschutz deaktiviert", "Password protection disabled"),
    NONE_VALID_PLAYER("Bitte gebe einen Spieler an der online ist", "Please provide a player that is online"),
    INCORRECT_PASSWORD("Falsches Passwort", "Incorrect password"),
    CORRECT_PASSWORD("Richtiges Passwort", "Correct password"),
    NO_PASSWORD_PROVIDED("Bitte gebe ein Passwort an", "Please provide a password"),
    PASSWORD_ON_TIMEOUT("Du hast das Passwort nicht rechtzeitig eingegeben", "You did not enter the password in time"),
    BAN_MESSAGE("Du wurdest vom Winterdorf gebannt, bitte kontaktiere einen Admin falls das ein Fehler war", "You have been banned from the Winterdorf, please contact an admin if this was a mistake"),
    ;

    private final String deTranslation;
    private final String enTranslation;

    Lang(String deTranslation, String enTranslation) {
        this.deTranslation = deTranslation;
        this.enTranslation = enTranslation;
    }

    public String getTranslation(Player player) {
        String language = player.getLocale().split("_")[0];
        if (language.equals("de")) {
            return deTranslation;
        } else {
            return enTranslation;
        }
    }
}
