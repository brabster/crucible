# Contributing

When contributing to this repository, please first discuss the change you wish to make via an issue.
That will save you spending time on something that won't be accepted.
Changes that break existing functionality will need especially careful consideration and may be rejected.

Please note we have a [code of conduct](CODE_OF_CONDUCT.md), please follow it in all your interactions with the project.

## Contributors: Pull Request Process

1. Please ensure new code produces a valid CloudFormation template.
1. Update [README.md](README.md) if you're adding something that could be useful to new folks or people browsing.
1. Create a pull request and a maintainer will take a look as soon as possible. You can see the process we follow below.

If you'd like to be a maintainer after making a couple of contributions,
please mention that on your PR or a comment to your PR.

## Maintainers: Merging and Releasing

### Current maintainers:
- [brabster](https://github.com/brabster)
- [keerts](https://github.com/keerts)
- [shooit](https://github.com/shooit)

1. Check that there's some kind of test coverage for code changes.
   The minimum is just to check that the new code produces a template without error.
   The build will check that if there's a test.
1. Ensure that the updates don't alter any existing functionality.
   Ask yourself whether any existing template could be broken by the change.
1. If no problems, merge the PR and ensure the build is successful.
1. If merge OK, draft a new release in the same form as previous releases and publish it.
   Remember to thank the contributor.
   The versioning scheme we use just increments the semver minor or patch versions as you think appropriate.

If the change doesn't pass the test coverage or breaking change checks,
use comments and/or code review to negotiate changes with the maintainer.
