# How to create a new project

## How to create a new SSH key on Windows

Open Git Bash. Check if you have some existing Pair keys.

```bash
ls -al ~/.ssh
```

### In case you do not have any pair keys

Generate a new Pair keys

```bash
ssh-keygen -t ed25519 -C "<your_email_address>"
```

You can check whether the Pair key have been created

```bash
ls -al ~/.ssh
```

They should be named:

```bash
id_ed25519
id_ed25519.pub
```

Now you need to add these pair keys to ssh-agent:

```bash
eval "$(ssh-agent -s)"
ssh-add ~/.ssh/id_ed25519
```

Then
follow [Github procedure](https://docs.github.com/en/enterprise-server@3.1/authentication/connecting-to-github-with-ssh/adding-a-new-ssh-key-to-your-github-account)
.

### In case you already have some pair keys

Check Github procedure above.

## Create a new repo

1. Login to your Github account.
2. At the top right of any Github page, you should see a '+' icon. Click that, then select 'New Repository'.
3. Give your repository a name (ideally the same name as your local project)
4. Click 'Create Repository'.

## Create a new repo locally

Create your repo locally.

In your terminal, set up git for this new repo:

```bash
cd repo/name
git init -b main # or just git init if you ran: git config --global init.defaultBranch main
git add .
git commit -m "initial commit"
```

## Connect your local repo to your remote repo

From github get your SSH address `git@github.com:<account>/<projectname>.git`

Update the remote branch from your terminal by running:

```bash
git remote add origin git@github.com:<account>/<projectname>.git
```

Push your branch to Github:

```bash
git push -u -f origin main
```
