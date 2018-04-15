# Money Track
A back-end for personal finance management. This is an `educational'
project which I'm trying to implement while learning web-development.
It's inspired by real expirience of tracking family finances for
several years.

## Development

1. Create a database on your PostgreSQL server
2. Specify the database connection parameters in src/data.clj 
3. To run a development server, execute `lein ring server-headless`
4. Alternatively, you can run server from REPL:
  ```clojure
  (use 'money-track.repl)
  (migrate)
  (start-server)
  ```

## Related projects

* https://github.com/olegknyazev/money-track-html

  A HTML front-end for this application.
