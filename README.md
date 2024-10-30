Ouline input which listens for incoming sms and getting their body.
Ideal for 2FA input making the user experience way better by creating a shortcut for inputing the code.

Usage:
    Add the neccesary permissions on Manifest file:
        <uses-feature android:name="android.hardware.telephony" android:required="false" />
        <uses-permission android:name="android.permission.RECEIVE_SMS"/>
    
    Create the SMSService class and create an instance of it and listen for input focus.