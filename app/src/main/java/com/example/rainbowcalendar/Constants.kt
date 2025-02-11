package com.example.rainbowcalendar

object Constants{
    const val key_package="com.example.rainbowcalendar_pref"

    /**string language code, e.g. pl, when using localized like pt-BR gotta make Locale by hand from pt,BR**/
    const val key_language="lang"
    const val key_theme="theme"
    /**int 0 - trans man 1 - nb, 2 - woman**/
    const val key_gender="gender"

    /** boolean, whether language was chosen**/
    const val key_isLanguageSetUp="langSetup"
    /** boolean, whether theme was chosen**/
    const val key_isThemeSetUp="themeSetup"
    /** boolean, whether recovery was chosen**/
    const val key_isRecoverySetUp="recoverySet"
    /** boolean, whether whole setup was completed**/
    const val key_isSetupDone="setupDone"
    /** boolean, whether period censor and stealth was chosen**/
    const val key_isStealthDone="stealthDone"
    /** boolean, whether consent (checkboxes for age etc.) menu is complete**/
    const val key_isConsentDone="consentDone"
    /** boolean, replace word "period" with "shark week" or another alternative**/
    const val key_censorPeriod="censorPeriod"

    /** whether is known if user is taking T or not not used for women**/
    const val key_testosteroneMenuComplete="tCompleted"
    /**boolean, whether user is currently taking testosterone**/
    const val key_isTakingTestosterone="isOnT"
    /**boolean, whether user is planning to take testosterone in future, if so, remind once in a while**/
    const val key_isPlanningToTakeTestosterone="planT"
    /**boolean, whether user is on lower dose of testosterone that usually doesn't stop period**/
    const val key_isMicrodosing="microdose"
    /** date of first dose of testosterone yyyy-mm-dd**/
    const val key_firstTestosteroneDate="firstT"
    /** date of last shot of testosterone yyyy-mm-dd**/
    const val key_lastTestosteroneDate="lastTDose"
    /** how many days between testosterone doses**/
    const val key_testosteroneInterval="testosteroneInterval"


    //region password and recovery
    /**string 1st recovery question content **/
    const val key_recoveryQuestion1="rQuestion1"
    /**string 2nd recovery question content **/
    const val key_recoveryQuestion2="rQuestion2"
    /**string 3rd recovery question content **/
    const val key_recoveryQuestion3="rQuestion3"
    /**string correct answer to 1st recovery question, is simplified later**/
    const val key_recoveryAnswer1="rAnswer1"
    /**string correct answer to 2nd recovery question, is simplified later**/
    const val key_recoveryAnswer2="rAnswer2"
    /**string correct answer to 3rd recovery question, is simplified later**/
    const val key_recoveryAnswer3="rAnswer3"

    /**int temporary pin, used in "confirm pin" screen **/
    const val key_temporaryPin="tempPin"
    /**int how many password/pin was input incorrectly **/
    const val key_failedAttempts="failedAttempts"

    /**int 0-select type of password 1-text password 2-pin code 4 digit**/
    const val key_passwordScreenType="passwordScreenType"
    /**int 0-create pin 1-confirm pin(creation) 2-enter pin **/
    const val key_pinScreenType="pinScreenType"
    /** password/pin - string**/
    const val key_passwordValue="passwordValue"
    //endregion
}