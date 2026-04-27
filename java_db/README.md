# NL to MongoDB (Java)

This is a CLI Java app that:
1. Accepts a plain-language request.
2. Sends it to OpenAI ChatGPT API.
3. Gets a JSON query plan (`find`, `countDocuments`, or `aggregate`).
4. Executes that query in MongoDB.
5. Prints the result.
6. Prints the JSON query plan returned by ChatGPT before execution.

## Requirements

- Java 17+
- Maven 3.9+
- MongoDB connection string
- OpenAI API key

## Project structure (standard Maven)

- Java code: `src/main/java`
- Resources/config: `src/main/resources`

## Config file

Main config is loaded from:
1. `APP_CONFIG_FILE` (if set)
2. `config.properties` in project root (if present)
3. `src/main/resources/config.properties` (classpath fallback)

Default Maven location is `src/main/resources/config.properties`:

```properties
openai.apiKey=YOUR_OPENAI_API_KEY
openai.model=gpt-4.1-mini
openai.systemPromptFile=system_prompt.txt
openai.schemaFile=schema_description.txt
mongodb.uri=mongodb://localhost:27017
mongodb.db=YOUR_DB_NAME
```

You can also use environment variables as fallback:

- `OPENAI_API_KEY`
- `OPENAI_MODEL`
- `MONGODB_URI`
- `MONGODB_DB`
- `OPENAI_SYSTEM_PROMPT_FILE`
- `OPENAI_SCHEMA_FILE`
- `APP_CONFIG_FILE` (optional custom path to config file)

## Prompt and schema files

- `src/main/resources/system_prompt.txt` - custom system rules.
- `src/main/resources/schema_description.txt` - database structure description.

These files can also be overridden by absolute/relative file paths in config.

The app also reads real collection names directly from MongoDB (`mongodb.db`) and sends them to the model, so collection is selected automatically.

## Run

Build:

```bash
./mvnw -q -DskipTests package
```

Run with request as arguments:

```bash
./mvnw -q exec:java -Dexec.args="show last 5 paid orders sorted by createdAt descending"
```

Or run without args and type the request when prompted:

```bash
./mvnw -q exec:java
```

Windows PowerShell equivalents:

```powershell
.\mvnw.cmd -q -DskipTests package
.\mvnw.cmd -q exec:java -Dexec.args="скільки всього занять у розкладі"
```

## Notes

- Only read-only operations are supported by design: `find`, `countDocuments`, `aggregate`.
- Collection is chosen by model from actual collections in the selected database.
- If MongoDB returns no visible collections, the model can still choose a collection name using `schema_description.txt`.
