package com.sih.sankatlink.model

/**
 * 10 Official Indian Languages (+ English) supported by SankatLink
 * for 100% offline on-device STT, Translation, and TTS.
 */
enum class IndianLanguage(
    val displayName: String,
    val nativeName: String,
    val isoCode: String,
    val scriptName: String
) {
    HINDI("Hindi", "हिन्दी", "hi", "Devanagari"),
    BENGALI("Bengali", "বাংলা", "bn", "Bengali"),
    TAMIL("Tamil", "தமிழ்", "ta", "Tamil"),
    TELUGU("Telugu", "తెలుగు", "te", "Telugu"),
    MARATHI("Marathi", "मराठी", "mr", "Devanagari"),
    GUJARATI("Gujarati", "ગુજરાતી", "gu", "Gujarati"),
    KANNADA("Kannada", "ಕನ್ನಡ", "kn", "Kannada"),
    MALAYALAM("Malayalam", "മലയാളം", "ml", "Malayalam"),
    ODIA("Odia", "ଓଡ଼ିଆ", "or", "Odia"),
    PUNJABI("Punjabi", "ਪੰਜਾਬੀ", "pa", "Gurmukhi"),
    ENGLISH("English", "English", "en", "Latin");

    companion object {
        val DEFAULT_SOURCE = HINDI
        val DEFAULT_TARGET = BENGALI

        /** Quick emergency phrasebook for 1-tap transmission when victims cannot speak */
        val EMERGENCY_PHRASES = listOf(
            EmergencyPhrase(
                id = "med_help",
                english = "Immediate medical assistance required!",
                hindi = "तत्काल चिकित्सा सहायता की आवश्यकता है!",
                bengali = "অবিলম্বে জরুরি চিকিৎসার সাহায্য প্রয়োজন!",
                tamil = "உடனடி மருத்துவ உதவி தேவைப்படுகிறது!",
                telugu = "తక్షణ వైద్య సహాయం అవసరం!",
                marathi = "तातडीने वैद्यकीय मदतीची गरज आहे!",
                gujarati = "તાત્કાલિક તબીબી સહાયની જરૂર છે!",
                kannada = "ತಕ್ಷಣದ ವೈದ್ಯಕೀಯ ನೆರವು ಅಗತ್ಯವಿದೆ!",
                malayalam = "അടിയന്തിര വൈദ്യസഹായം ആവശ്യമാണ്!",
                odia = "ତୁରନ୍ତ ଡାକ୍ତରୀ ସହାୟତା ଆବଶ୍ୟକ!",
                punjabi = "ਤੁਰੰਤ ਡਾਕਟਰੀ ਸਹਾਇਤਾ ਦੀ ਲੋੜ ਹੈ!"
            ),
            EmergencyPhrase(
                id = "trapped",
                english = "People trapped under rubble/debris here!",
                hindi = "यहाँ मलबे के नीचे लोग दबे हुए हैं!",
                bengali = "এখানে ধ্বংসস্তূপের নিচে মানুষ আটকে আছে!",
                tamil = "இங்கு இடிபாடுகளுக்குள் மனிதர்கள் சிக்கியுள்ளனர்!",
                telugu = "ఇక్కడ శిథిలాల క్రింద ప్రజలు చిక్కుకున్నారు!",
                marathi = "येथे ढिगाऱ्याखाली लोक अडकले आहेत!",
                gujarati = "અહીં કાટમાળ નીચે લોકો ફસાયેલા છે!",
                kannada = "ಇಲ್ಲಿ ಅವಶೇಷಗಳ ಕೆಳಗೆ ಜನರು ಸಿಲುಕಿಕೊಂಡಿದ್ದಾರೆ!",
                malayalam = "ഇവിടെ അവശിഷ്ടങ്ങൾക്കിടയിൽ ആളുകൾ കുടുങ്ങിക്കിടക്കുന്നു!",
                odia = "ଏଠାରେ ଧ୍ୱଂସାବଶେଷ ତଳେ ଲୋକମାନେ ଫସି ରହିଛନ୍ତି!",
                punjabi = "ਇੱਥੇ ਮਲਬੇ ਹੇਠਾਂ ਲੋਕ ਫਸੇ ਹੋਏ ਹਨ!"
            ),
            EmergencyPhrase(
                id = "flood_rising",
                english = "Water level rising rapidly! Send rescue boat!",
                hindi = "पानी का स्तर तेजी से बढ़ रहा है! नाव भेजें!",
                bengali = "জল দ্রুত বাড়ছে! উদ্ধারকারী নৌকা পাঠান!",
                tamil = "தண்ணீர் மட்டம் உயர்கிறது! மீட்பு படகு அனுப்பவும்!",
                telugu = "నీటి మట్టం వేగంగా పెరుగుతోంది! రెస్క్యూ బోట్ పంపండి!",
                marathi = "पाण्याची पातळी वेगाने वाढत आहे! बचाव बोट पाठवा!",
                gujarati = "પાણીનું સ્તર ઝડપથી વધી રહ્યું છે! બચાવ બોટ મોકલો!",
                kannada = "ನೀರಿನ ಮಟ್ಟ ವೇಗವಾಗಿ ಏರುತ್ತಿದೆ! ರಕ್ಷಣಾ ದೋಣಿ ಕಳುಹಿಸಿ!",
                malayalam = "വെള്ളപ്പൊക്കം ഉയരുന്നു! രക്ഷാ ബോട്ട് അയക്കൂ!",
                odia = "ଜଳସ୍ତର ଦ୍ରୁତ ଗତିରେ ବୃଦ୍ଧି ପାଉଛି! ଉଦ୍ଧାର ଡଙ୍ଗା ପଠାନ୍ତୁ!",
                punjabi = "ਪਾਣੀ ਦਾ ਪੱਧਰ ਤੇਜ਼ੀ ਨਾਲ ਵਧ ਰਿਹਾ ਹੈ! ਬਚਾਅ ਕਿਸ਼ਤੀ ਭੇਜੋ!"
            ),
            EmergencyPhrase(
                id = "food_water",
                english = "Drinking water and food shortage at this camp.",
                hindi = "इस राहत शिविर में पीने के पानी और भोजन की कमी है।",
                bengali = "এই ত্রাণ শিবিরে খাবার ও পানীয় জলের সংকট।",
                tamil = "இந்த முகாமில் குடிநீர் மற்றும் உணவு தட்டுப்பாடு உள்ளது.",
                telugu = "ఈ శిబిరంలో తాగునీరు మరియు ఆహార కొరత ఉంది.",
                marathi = "या मदत छावणीत पिण्याचे पाणी आणि अन्नाचा तुटवडा आहे.",
                gujarati = "આ શિબિરમાં પીવાના પાણી અને ખોરાકની અછત છે.",
                kannada = "ಈ ಶಿಬಿರದಲ್ಲಿ ಕುಡಿಯುವ ನೀರು ಮತ್ತು ಆಹಾರದ ಕೊರತೆಯಿದೆ.",
                malayalam = "ഈ ക്യാമ്പിൽ കുടിവെള്ളത്തിനും ഭക്ഷണത്തിനും ക്ഷാമമുണ്ട്.",
                odia = "ଏହି ଶିବିରରେ ପିଇବା ପାଣି ଏବଂ ଖାଦ୍ୟର ଅଭାବ ଅଛି।",
                punjabi = "ਇਸ ਕੈਂਪ ਵਿੱਚ ਪੀਣ ਵਾਲੇ ਪਾਣੀ ਅਤੇ ਭੋਜਨ ਦੀ ਘਾਟ ਹੈ।"
            ),
            EmergencyPhrase(
                id = "all_safe",
                english = "All survivors safe and accounted for here.",
                hindi = "यहाँ सभी लोग सुरक्षित हैं।",
                bengali = "এখানে সবাই নিরাপদে আছেন।",
                tamil = "இங்கு அனைவரும் பாதுகாப்பாக உள்ளனர்.",
                telugu = "ఇక్కడ అందరూ సురక్షితంగా ఉన్నారు.",
                marathi = "येथे सर्वजण सुरक्षित आहेत.",
                gujarati = "અહીં બધા સુરક્ષિત છે.",
                kannada = "ಇಲ್ಲಿ ಎಲ್ಲರೂ ಸುರಕ್ಷಿತವಾಗಿದ್ದಾರೆ.",
                malayalam = "ഇവിടെ എല്ലാവരും സുരക്ഷിതരാണ്.",
                odia = "ଏଠାରେ ସମସ୍ତେ ସୁରକ୍ଷିତ ଅଛନ୍ତି।",
                punjabi = "ਇੱਥੇ ਸਾਰੇ ਲੋਕ ਸੁਰੱਖਿਅਤ ਹਨ।"
            )
        )
    }
}

data class EmergencyPhrase(
    val id: String,
    val english: String,
    val hindi: String,
    val bengali: String,
    val tamil: String,
    val telugu: String,
    val marathi: String,
    val gujarati: String,
    val kannada: String,
    val malayalam: String,
    val odia: String,
    val punjabi: String
) {
    fun getText(lang: IndianLanguage): String = when (lang) {
        IndianLanguage.HINDI -> hindi
        IndianLanguage.BENGALI -> bengali
        IndianLanguage.TAMIL -> tamil
        IndianLanguage.TELUGU -> telugu
        IndianLanguage.MARATHI -> marathi
        IndianLanguage.GUJARATI -> gujarati
        IndianLanguage.KANNADA -> kannada
        IndianLanguage.MALAYALAM -> malayalam
        IndianLanguage.ODIA -> odia
        IndianLanguage.PUNJABI -> punjabi
        IndianLanguage.ENGLISH -> english
    }
}

