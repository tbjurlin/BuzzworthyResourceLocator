# Checklist for reviewing code

1. On your local machine, pull branch to review `git pull origin/branch_name`
2. Review the code for the following criteria:
    1. [ ] The code works:
        1. [ ] The code runs.
        2. [ ] The code meets the requirements of the feature.
    2. [ ] The code is secure:
        1. [ ] No vulnerabilities are introduced.
        2. [ ] All data passing the trust boundary is sanitized.
        3. [ ] All data passing the trust boundary is validated.
        4. [ ] Prepared database queries are used, if applicable.
    3. [ ] The code is maintainable:
        1. [ ] All tests in the code pass.
        2. [ ] All properties of the code are tested.
        3. [ ] All of the code is documented clearly. This means that you clearly understand what every part of the code does. If you are less familiar with the language, ask the author questions. The goal is to have the code documented such that a person familiar with the language can clearly understand _why_ the code has been written.
        4. [ ] Team has been notified of any major API changes that may impact other features being worked on.
        5. [ ] The code meets agreed upon style guidelines.
    4. [ ] The code is efficient:
        1. [ ] Recursion is not used unless absolutely necessary.
        2. [ ] Recursive calls definetly terminate.
        3. [ ] Recursive calls do not branch unless absolutely necessary.
        4. [ ] Data is not mutated while being iterated on.
        5. [ ] No other obvious inefficienies are being introduced.
3. Determine if the code passes the criteria.
    a. If it passes, go to https://github.com/tbjurlin/BuzzworthyResourceLocator and approve the pull request.
    b. If it fails, go to https://github.com/tbjurlin/BuzzworthyResourceLocator and reject the pull request with a detailed description of the reason why. It is important to put this description in the pull request, even if you tell the person directly so that the reason is recorded.