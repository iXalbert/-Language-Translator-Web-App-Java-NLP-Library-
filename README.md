# Language Translator Web App — Java & NLP

A Spring Boot web application that translates text between languages using the **Apache OpenNLP** library for natural language processing. The app performs tokenization, POS tagging, and lemmatization to produce grammatically-aware translations driven by custom bilingual dictionaries.

## Features

- Translate text between **English** and **German** → **Romanian**
- NLP pipeline per language: tokenizer → POS tagger → lemmatizer → dictionary lookup
- Verb conjugation logic for Romanian present tense and perfect compus (past tense)
- Result **caching** with Caffeine (repeated identical requests are served instantly)
- REST API with a clean JSON interface
- Built-in Swagger / OpenAPI UI
- Simple, responsive HTML/Tailwind CSS frontend served at the app root

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| NLP | Apache OpenNLP 2.2 |
| Caching | Spring Cache + Caffeine |
| API Docs | springdoc-openapi (Swagger UI) |
| Build | Maven (mvnw wrapper included) |
| Testing | JUnit 5, Mockito |

## Project Structure

```
src/
└── main/
    ├── java/com/translatorapp/.../
    │   ├── LanguageTranslatorWebAppJavaNlpLibraryApplication.java  # Entry point
    │   ├── config/
    │   │   ├── NlpConfig.java          # Loads OpenNLP models (tokenizer, POS, lemmatizer)
    │   │   └── OpenApiConfig.java      # Swagger/OpenAPI configuration
    │   ├── controller/
    │   │   └── TranslationController.java  # POST /api/v1 endpoint
    │   ├── exception/
    │   │   ├── GlobalExceptionHandler.java
    │   │   └── TranslationValidationException.java
    │   ├── model/
    │   │   ├── TranslationRequest.java
    │   │   └── TranslationResponse.java
    │   └── service/
    │       ├── TranslationDictionary.java  # Loads CSV dictionaries at startup
    │       └── TranslationService.java     # NLP pipeline + translation logic
    └── resources/
        ├── static/index.html           # Frontend UI
        ├── models/                     # Lemmatizer dictionaries (.dict)
        ├── pos/                        # POS tagger models (.bin)
        ├── token/                      # Tokenizer models (.bin)
        ├── translation_data_en.txt     # English → Romanian dictionary
        └── translation_data_de.txt     # German → Romanian dictionary
```

## Getting Started

### Prerequisites

- **Java 17** or later
- **Maven** (or use the included `./mvnw` wrapper — no installation needed)

### Run the Application

```bash
./mvnw spring-boot:run
```

Once started, open your browser at:

- **Frontend UI:** `http://localhost:8080`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Build a JAR

```bash
./mvnw clean package
java -jar target/*.jar
```

## API Reference

### `POST /api/v1`

Translates the given text from the source language into Romanian.

**Request body (JSON):**

```json
{
  "sourceText": "I see the house",
  "sourceLang": "en",
  "targetLang": "ro"
}
```

| Field | Type | Description |
|---|---|---|
| `sourceText` | `string` | Text to translate (required, non-empty) |
| `sourceLang` | `string` | Source language code: `en` or `de` |
| `targetLang` | `string` | Target language code (currently `ro`) |

**Success response (200 OK):**

```json
{
  "translatedText": "Eu văd casa",
  "statusMessage": "Traducerea efectuata cu succes"
}
```

**Error response (400 Bad Request):**

```json
{
  "statusMessage": "Textul sursa nu poate fi gol"
}
```

## Dictionary Format

Dictionary files (`translation_data_en.txt`, `translation_data_de.txt`) use a simple CSV format:

```
<source_word>,<POS_tag>,<romanian_translation>
```

Example:
```
see,VBP,a vedea
house,NN,casa
i,PRP,eu
```

POS tags follow the Penn Treebank convention (e.g. `NN`, `VBP`, `VBZ`, `JJ`, `RB`).

## Running Tests

```bash
./mvnw test
```

Test coverage includes:
- `TranslationServiceTest` — unit tests for the translation pipeline
- `TranslationDictionaryTest` — dictionary loading and lookup
- `TranslationControllerTest` — controller layer (MockMvc)
- `TranslationRequestTest` / `TranslationResponseTest` — model tests

## Caching

Translation results are cached by Spring Cache (backed by Caffeine). Identical `(sourceText, sourceLang, targetLang)` combinations are returned from cache on subsequent calls without re-running the NLP pipeline.

## Limitations & Known Issues

- Only **English** (`en`) and **German** (`de`) are supported as source languages at present; the target language is always **Romanian** (`ro`).
- Translation quality depends entirely on the contents of the dictionary files — words not present will be passed through untranslated.
- The frontend language selector shows French (`fr`) and Romanian (`ro`) as source options, but these are not yet backed by NLP models or dictionaries.
