# OCRAPI (Oracle)
## Introduction
**Oracle** is the pet name of this project the term **OCRAPI** will be the
official project code. *don't get confused wit the name*

**Oracle** is a **REST API** capable of accepting request's to process and extract
content from **Images** to extract pre defined set of fields from the documents
provided with using *Optical Character Recognition* technique. **Oracle** will
not perform any special validations over the extracted filed, rather
it will provide the extracted results.


## Using GIT
All development should follow the [**Git Flow**](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow). `master` and `develop` branches
will be protected. Developers can create feature branches at their will

Please pay attention to the following when creating feature branches.
* Always create the feature branch form the current develop branch
* Try to be informative as possible when selecting a name to the feature branch
* You can push breaking changes to your remote feature branch but **do not**
 merge it with the `develop` or `master` branches.
* Once you are done implementing `do not` right away merge your changes to `develop`
branch rather send a `merge request` from the **gitlab** web interface. This will
give a chance for a peer developer to have a simple peer review on the features
developed.
* When merging a feature branch with `develop` branch always **delete** the
feature branch after merging with the `develop`

Also please pay attention to the following regarding the committing.

* Take updates from `remote` as often as you can *at least each day, before you
 start development*
* Commit with small changes. Don't wait till the end to commit with large
number of changes rather commit with small changes, With each commit take
an update from `remote`

`git branch -b <<Feature>> develop`

## Build and Dependency Management
**Oracle** will use [**Gradle**](https://gradle.org/) as the dependency management and build tool.
The *build.gradle* file you find in the root of the project will be the root
build file and the sub modules will contain their own *build.gradle* file.

#### Dependency Management
If a new dependency needs to be added update the following section in the
*build.gradle* file
```
dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.11'
}
```
#### Build Management and Versioning
**Oracle** use the [**Semantic Versioning**](http://semver.org/). To make the build and version
management easy we will be using [**Semantic Version Plugging**](https://github.com/vivin/gradle-semantic-build-versioning). It will
expose number of task which can be used to build and automatically change the
version number based on the semantic version logic.

Also it will also create the proper `tag` for the release.

A gradle task may look like this

`gradle clean build release tag bumpMajor`

Go through the plugin documentation and semantic versioning before start
a release phase.


## Module Structure
**Oracle** has the following project structure
```
Oracle
    +--- ApolloAgent
    +--- DataTransferObjects
    +--- JobMessagingChannelHandler
    +--- OracleAPI
    +--- OracleDataAbstraction
    \--- Services
        +--- API
        +--- Apollo
        +--- Core
```

##### Oracle
This is the root project. All sub modules will be included in to this root project

##### ApolloAgent
**ApolloAgent** is the image processing agent, which listen to the message queue for new processing requests
once a processing request is received it will process the images using the native libraries.
This agent will be deployed as a windows service with the help of apache commons demon.

All the implementation related to management of the agent must go under this module. However business
logic related to the agent should not be implemented under this sub module rather it should go under
the **service** sub module.

##### DataTransferObjects
This sub module holds the implementation of the POJO classes used in transferring details from one
module to another module or sending data to the outside entities using web API.

##### JobMessagingChanelHandler
All the implementation related to connecting to the message queue and submitting and receiving new
messages should go under this module. This module will be injected to all the consumers and
producers as a dependency. Relevant methods will be available for producers and consumers to
add a new message to the queue or listen to new messages.

#### OracleAPI
This is the **REST API** which will be exposed to the external parties to interact with the service.
Once a request is received to the api it should delegate the request to the corresponding service
class to handle the request appropriately. Do not add any business logic to the controller implementation,
rather move them to the service layer properly.

#### Services
All the implementation regarding the

