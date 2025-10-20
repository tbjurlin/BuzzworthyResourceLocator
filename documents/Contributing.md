# Process for adding new feature

1. On your local machine, create new branch `git checkout -b new_branch_name`
2. make edits and commit ...
3. When ready to merge, pull from main `git pull origin/main`
4. Fix any merge conflicts on your local machine.
5. Use `git push -u origin new_branch_name` to publish to the remote.
6. Go to https://github.com/tbjurlin/BuzzworthyResourceLocator.
7. Select `Pull Requests` -> `Add New Pull Request`
8. Select `main` as the base branch and `new_branch_name` as the compare branch.
9. If there are merge conflicts, go back to step 3.
10. Write a description of the features added or bugs fixed in the pull request. Indicate any related issues, if necessary.
11. Add designated reviewer.
12. Create pull request.
13. Wait for approval.
14. Once approved, merge and close the pull request.
    a. If it isn't approved, make the required fixes to meet approval, repeat steps 3 to 5, then request a new approval.