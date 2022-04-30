# sqldelight-js-worker

Proof of concept of SQLDelight sqljs-driver running in a web worker.

## Building and Running

This depends on [a fork](https://github.com/dellisd/sqldelight/tree/async-drivers) of SQLDelight that adds (basic) support for async drivers.
A build of that fork must be available in `mavenLocal()`.

Once the SQLDelight snapshot is built and installed in your local maven repository, you should be able to build and run this project using this command:

```shell
./gradlew :jsBrowserRun
```

This will open a browser window at `http://localhost:8080` and you should see a list of names that were queried from the sql.js worker database.