- TODO: add IdTerm, IdWriter, IdScreen
- TODO: add logging to ZIO
- TODO: fix ZIO event-loop -- no exit after quit
= TODO: add LogScreen that logs execution


- TODO: refactor util, term, zio classes, move them to the correct directories

- TODO: refactor term names


= TODO: ZIO example -- errors are not visible anyhow, the screen is black.

- TODO: improve debugging

- TODO: update readme pages


  private def appendToLog(text: String): Unit = {
    java.nio.file.Files.write(java.nio.file.Paths.get("/home/gchudnov/Projects/swearwolf/target/log.txt"), s"${java.time.LocalDateTime.now()}: ${text}\n".getBytes(), java.nio.file.StandardOpenOption.APPEND)
  }
