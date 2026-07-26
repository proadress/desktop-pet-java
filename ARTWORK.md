# Artwork provenance

The coral-orange robot fox in `artwork/robot-fox-source.png` was created for
this repository on 2026-07-26 with OpenAI's built-in image generation tool. It
was generated as new artwork without using an input image.

The transparent source was produced with a flat chroma-key background and the
repository's runtime images were then generated deterministically by
`tools/GenerateArtwork.java`.

## Generation prompt

> Create one entirely original small coral-orange robot fox mascot, side view
> facing right, in a lively mid-run pose. Use a polished, crisp,
> pixel-art-inspired 2D game-sprite style with clean solid shapes and a readable
> silhouette. Give it a large expressive tail, rounded navy-blue mechanical
> joints, a cream face and belly, and a small cyan status light. Center exactly
> one full-body character on a perfectly flat green chroma-key background with
> generous padding. Do not include text, logos, shadows, scenery, weapons,
> clothing, or resemblance to an existing game, anime, cartoon, or brand
> character.

## Derived files

- `picture/1.png` through `picture/6.png`: primary animation frames
- `picture/robot-fox-plugin.png`: 4-by-2 example plugin sprite sheet
- `picture/run.png` and `picture/stop.png`: original programmatic tray icons

Regenerate them with Java 17 or newer:

```bash
./gradlew generateArtwork
```
