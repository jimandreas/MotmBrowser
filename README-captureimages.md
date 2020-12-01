# Capture Images application

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

