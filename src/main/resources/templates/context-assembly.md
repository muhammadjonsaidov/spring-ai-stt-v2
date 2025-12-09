```java
// 1. TILNI ANIQLASH (Eng muhimi)
// AssemblyAI "Universal-1" modeli tilni juda yaxshi aniqlaydi.
body.put("language_detection", true);

// 2. CONTEXT BERISH (Prompt o'rniga)
// Bu yerda biz modelga "shpargalka" beramiz. 
// Loyihangizga oid terminlar, ismlar yoki aralashishi mumkin bo'lgan so'zlar.
List<String> keywords = List.of(
        "O'zbekiston", "Toshkent", "Assalomu alaykum",
        "Spring Boot", "Java", "Gemini", "Groq", "API",
        "Sales Doctor", "Transkripsiya"
);
        body.put("word_boost", keywords);
        body.put("boost_param", "high"); // 'low', 'default', 'high'

// 3. Tinish belgilari
        body.put("punctuate", true);
        body.put("format_text", true);

// Agar spikerlar 2 kishi bo'lsa, ularni ajratish (Speaker Diarization)
// body.put("speaker_labels", true);
```