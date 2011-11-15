package nandcat;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Manages the internationalization of the application using resource bundles.
 */
public final class I18N {

    /**
     * Currently selected locale.
     */
    private static Locale locale = Locale.GERMAN;

    /**
     * Private constructor prevents instantiation from outside.
     */
    private I18N() {
    }

    /**
     * Gets the ResourceBundle depending on the current locale.
     * 
     * @param section
     *            Resource section.
     * @return ResourceBundle to get text from.
     */
    public static I18N.I18NBundle getBundle(String section) {
        ResourceBundle resBundle = ResourceBundle.getBundle(section, locale);
        return new I18N().new I18NBundle(locale, resBundle);
    }

    /**
     * Gets the currently selected locale.
     * 
     * @return Current locale.
     */
    public static Locale getLocale() {
        return locale;
    }

    /**
     * Represents the translation unit with a specific locale and resourcebundle.
     */
    public final class I18NBundle {

        /**
         * The selected locale.
         */
        private Locale locale;

        /**
         * The selected ResourceBundle.
         */
        private ResourceBundle bundle;

        /**
         * Creates an instance of the translator. Private, should only be accessed from I18N.
         * 
         * @param locale
         *            Locale of translation.
         * @param bundle
         *            ResourceBundle to get translation from.
         */
        private I18NBundle(Locale locale, ResourceBundle bundle) {
            this.locale = locale;
            this.bundle = bundle;
        }

        /**
         * Gets the translated and formatted string from the bundle.
         * 
         * @see java.text.MessageFormat
         * @param key
         *            Key inside the bundle.
         * @param messageArguments
         *            Compound messages arguments.
         * @return Translated and formatted string.
         */
        public String getString(String key, Object... messageArguments) {
            MessageFormat formatter = new MessageFormat("");
            formatter.setLocale(locale);
            formatter.applyPattern(bundle.getString(key));
            return formatter.format(messageArguments);
        }
    }
}
