## BRL Project Meeting Notes (Team Yellow)
These notes outline the decisions made during team meetings to be later dissemenated into project documentation.

### 10/17/25
<b>Vision Document Presentation Retrospective:</b>
- Add use cases for:
    - Flag record for manager review with comment
    - Managers can clear flags
    - Add/remove user's own upvote from record
        - Could be extended later for downvotes
    - Search records by date added
    - Search records by flagged

---

### 10/20/25 

<b>Design Phase To-Do's: (due by 10/24)</b>
- [x] Decide on language 
- [x] Determine naming conventions
- [x] Choose a database
- [ ] Lay out file structure (# of classes, DAOs, etc.)
- [ ] Create high-level design diagrams (CD and DFD)
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

<b>Still To-Do: (due by 10/24)</b>
- [ ] Lay out file structure (# of classes, DAOs, etc.)
- [ ] Create high-level design diagrams (CD and DFD)
- [ ] Run proofs of concept (e.g. does coding language interact with DB?)
- [ ] Create design presentation
- [x] [NEW] Decide on a platform for automation/testing
- [ ] [NEW] Decide on a web server

<b>Automated Testing Platform - Maven:</b>
- Works well with Java
- Straightforward installation and project integration
- Lots of plug-ins
- Customizable and detailed reports
- VS Code Extension

<b>What does a record contain?</b>
- index
- url
- date added
- poster employee id
- poster name
- title
- description
- isEdited flag (later release)
- editor employee id (later release)
- editor name (later release)
- list of comments
    - commenter employee id
    - commenter name
    - comment
    - date added
    - isEdited flag (later release)
- list of upvotes
    - upvoter employee id
    - upvoter name
- list of managerFlags
    - flagger employee id
    - flagger name
    - comment
    - date added

<b>Note: We are now considering using Java Properties instead of JSON for config files</b>

<b>Web Server - Tomcat?</b>

<b>Current task priority:</b>
1. Flesh out detailed use cases
2. Use the use cases to make Data Flow Diagrams (DFD)
3. From the DFD processes, approximate which classes will be needed

---

## 10/22/25

<b>Still To-Do: (due by 10/24)</b>
- [ ] Lay out file structure (# of classes, DAOs, etc.)
- [x] [NEW] Complete detailed use cases
- [x] Create high-level design diagrams (CD and DFD)
- [ ] Run proofs of concept (e.g. does coding language interact with DB?)
- [ ] Create design presentation
- [ ] Decide on a web server
- [ ] [NEW] Choose logging framework (and run prototype)
- [x] [NEW] Come up with project name
- [x] [NEW] Discuss documentation strategy

<b>How do users delete records?</b>
- A search could return indices with records, and a delete would take an index as input
- Potential danger for accidental deletion due to index typos?

<b>Logging - Log4j? Java Utility Logging?</b>
Reasons for Log4J:
- Log4j has more flexibility with config file and more functionality than JUL
- Large user base and indication of continued support
- More advanced filters than JUL and asychronous logging
- Built to be easily swapped out if needed

<b>Project Name - Buzzworthy Resource Locator</b>

<b>Documentation</b>
- User Manual
    - Can be docx, HTML, etc.
- Operation Manual
    - Can be docx, HTML, etc.
- Source Code Documentation
    - Javadoc for inline documentation generation
- Deployment Notes
    - Can de docx, HTML, etc.

<b>Mongo DB Atlas:</b>
- faster cloud-based option
- easy to scale as needed
- need to document potential costs ($0.008/hr ?)

---

### 10/23/25

<b>Still To-Do: (due by 10/24)</b>
- [ ] Lay out file structure (# of classes, DAOs, etc.)
- [x] Run proofs of concept (e.g. does coding language interact with DB?)
- [x] Create design presentation
- [x] Decide on a web server
- [x] [NEW] Choose logging framework (and run prototype)
- [x] [NEW] Finish data dictionary

<b>Completed design presentation today and performed successful prototypes for logging, connecting to MongoDB, and connecting to the authentication server via REST API. See DesignPresentation.pptx for more details.</b>

---

### 10/24/25

<b>Still To-Do: (due by 10/24 - today)</b>
- [X] Lay out file structure (# of classes, DAOs, etc.)
- [x] Determine development roles/duties (i.e. who is coding what?)

<b>We presented our design presentation, laid out our file structure, and designated roles for next week's coding phase.</b>

<b>Development Duties:</b>
- Ben
    - API (owner)
    - Authentication (owner)
    - Database (reviewer)
- Claire
    - Database (owner)
    - Security (reviewer)
    - Log Management (reviewer)
- Dennis
    - Business Logic (owner)
    - API (reviewer)
    - Config Management (reviewer)
- Janniebeth
    - Security (owner)
    - Authentication (reviewer)
- Ted
    - Log Management (owner)
    - Config Management (owner)
    - Business Logic (reviewer)

<b>Other notes:</b>
- Need to use log4j 2.17 and newer
    - Can use Maven to identify Log4j2 vulnerabilities
- For security, don't use direct string concatenation
    - Instead, use String.format()

---

### 10/27/25

<b>REST API Request Formats:</b>
- Retrieve all resource records
    - `GET @ http://localhost:8080/BRL/api/wiki/resource`
- Add a resource record
    - `POST @ http://localhost:8080/BRL/api/wiki/resource`
- Add a comment to a resource record
    - `POST @ http://localhost:8080/BRL/api/wiki/resource/{resourceId}/comment` 
- Add an upvote to a resource record
    - `POST @ http://localhost:8080/BRL/api/wiki/resource/{resourceId}/upvote`
- Add a manager review flag to a resource record
    - `POST @ http://localhost:8080/BRL/api/wiki/resource/{resourceId}/reviewFlag`
- Delete a resource record by its ID
    - `DELETE @ http://localhost:8080/BRL/api/wiki/resource/{resourceId}`
- Delete a comment on a resource record by its ID
    - `DELETE @ http://localhost:8080/BRL/api/wiki/resource/{resourceId}/comment/{commentId}`
- Delete an upvote on a resource record by its ID
    - `DELETE @ http://localhost:8080/BRL/api/wiki/resource/{resourceId}/upvote/{upvoteId}`
- Delete a manager review flag on a resource record by its ID
    - `DELETE @ http://localhost:8080/BRL/api/wiki/resource/{resourceId}/reviewFlag/{flagId}`

---

### 10/28/25

<b>Using Spring Boot 3.5.7 to deploy project via Tomcat</b>
- Simplifies the deployment process
- Widely used with extensive support and documentation
- Simple addition to pre-existing Maven project

---