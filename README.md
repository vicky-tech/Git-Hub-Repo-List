# README #

This README would normally document whatever steps are necessary to get your application up and running.

### What is this repository for? ###

* fetching all the repository from the url https://api.github.com/orgs/
* show the detail of repository #  open_issues_count, license, permissions, name and description
* once downloaded, user is able to see data offline as well

### Application Arch ###

* We have followed MVVM arch. pattern , offline first arch. as we need to show data offline as well
* followed single Activity Concept with Navigation
* viewModel # to handle view State , its lifecycle aware so id did not reload data from db on config change
* Data binding # to bind data to view , more readability, less code
* use Room ORM for persistent data
* Kotlin Coroutine # for async processing
* Retrofit # for rest api

### Future Scope###
* need to add rate limit as it's open api , we can have only limited call to server

### Kotlin Feature ######
*  Kotlin Coroutine for async task
*  Extension and scope function for utility and make code more readable
*  lateinit/ lazy initialization
*  when for switch case
* suspend function



