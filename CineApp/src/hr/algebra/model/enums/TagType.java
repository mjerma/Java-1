/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model.enums;

import java.util.Optional;

/**
 *
 * @author Mihael
 */
public enum TagType {
    ITEM("item"),
    TITLE("title"),
    PUB_DATE("pubDate"),
    DESCRIPTION("description"),
    ORIG_TITLE("orignaziv"),
    DIRECTOR("redatelj"),
    ACTORS("glumci"),
    DURATION("trajanje"),
    GENRE("zanr"),
    IMG_URL("plakat"),
    LINK("link"),
    START_DATE("pocetak");
    
    private final String name;

    private TagType(String name) {
        this.name = name;
    }

    public static Optional<TagType> from(String name) {
        for (TagType value : values()) {
            if (value.name.equals(name)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}
