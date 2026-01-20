# Capture Images application

## What it does

This app displays a thumnail sized version of a PDB file in 3D, and then takes a screen cap of the
thumbnail.  The screencap is then saved to a PNG file.

This was used to generate the database of PDB thumbnails used in the Molecule of the Month browser
app.   The images are saved in the [Kotmol image repo].

## Setup:

Navigate on the target Android device to the path:

`Internal shared storage\Android\data\com.bammellab.captureimages\cache`

Create the directories if not present:

```
cache
cache/PDB
cache/Thumbs
```

Copy the uncompressed PDB files from the list in `MotmPdbNames` to the PDB directory created above.

Run the app.  The app will cycle through the PDB files and display them as a small
3D image in ribbon mode.   It will then take a screen snapshot and convert it to
a PNG file with transparency.  Then it will write the thumbnail PNG image to the
`Thumbs` folder.


[motm image repo]:https://github.com/jimandreas/MotmImages

