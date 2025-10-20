## BRL Project Meeting Notes (Team Yellow)
These notes outline the decisions made during team meetings to be later dissemenated into project documentation.

---

### 10/20/25 

<b>Design Phase To-Do's: (due by 10/24)</b>
- [x] Decide on language 
- [x] Determine naming conventions
- [x] Choose a database
- [ ] Lay out file structure (# of classes, DAOs, etc.)
- [ ] Create a high-level design diagrams (CD and DFD)
- [x] Decide on config file type
- [ ] Run proofs of concept (e.g. does coding language interact with DB?)
- [ ] Create design presentation

<b>Programming Language - Java 25 LTS:</b>
- Allows for access modifiers unlike Python (public, private, etc.)
- Recent LTS with minimum of 5 years of support
- Inherently object-oriented, allowing for modularity and maintainability
- Robust framework and simplified access to trusted libraries

<b>Naming Conventions:</b>
- Following Java standards:
    - lower camel case for variables
    - upper camel case for class names/functions/methods
    - upper camel case for file names
    - all lowercase, no spaces for directories

<b>High-Level Diagrams:</b>
- External view (Context Diagram - CD) shows how REST API host server interacts with authentication server, DB server, and front-end/user
- Internal view (Data Flow Diagram - DFD) shows how the BRL's internal modules communicate
    - This view can be reused to show the individual decision paths of each use case

<b>Config file type - JSON:</b>
- smaller file size than XML
- already using JSON for authentication server and to return objects to the front-end/user
- widely used and supported

<b>Database - Mongo DB:</b>
- compatability with Java and Python
- text indexing
- open-source but well-established
- horizontal scalability
- BSON to JSON conversion
- rapid insertion for future batch database population
- more maintainable and extensible than MySQL (less time and expense establishing and migrating schemas)
- specific permissions over datasets for security
- TLS encrypted communications

---

### 10/21/25