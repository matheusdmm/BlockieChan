# BlockieChan

A very basic blockchain in java made to understand and try some things within this technology.

Also, in the last version I've some minor optimizations from the inspired project, mostly on loops and logic operators to clean a bit the code.

## Usage

There isn't much usage to this beyond learn and see how it works, but for it you gonna need a java ide and to point the two libraries appended to the project (gson & Bouncy Castle) to compile it, otherwise it wont work.

Also, the difficulty is setted to 3 from the start, which is pretty light on a 8 core processor, but if you want to really challenge your computer, you can change it to like 15 and its gonna take a while to mine the blocks.

```bash
Use the force and get your favorite java IDE for this thing.

You gonna need to set the Bouncy Castle and the Gson files within your IDE.

Aaaaand you gonna need to have patience if you run it with a higher difficulty.
```

## What is working

- Generate Blocks
- Verify Blocks
- Create wallets
- Transactions
- Public and private keys
- Ownership of blocks

It was made using [IntelliJ IDEA](https://www.jetbrains.com/idea/), [GSON library](https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar), [Bouncy Castle](https://www.bouncycastle.org/latest_releases.html) and [Based on NoobChain by CryptoKass](https://github.com/CryptoKass/NoobChain-Tutorial-Part-1)