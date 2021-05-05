# LWJGLTesting
A sort of test LWJGL repository, to test how I would want to set this up.

## Purpose
The purpose of this project is to learn how to make a 3D rendering engine using OpenGL and by extension LWJGL. It followed a [YouTube tutorial series](https://www.youtube.com/playlist?list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP) by [TheThinMatrix on YouTube](https://www.youtube.com/user/ThinMatrix).

The project does include personally desired features, including a couple different rendering scenes. Further, this project did some things differently from the YouTube series, including window management with GLFW.

## Commit message key
Some commit messages contain more than a message. This information is usually enclosed in either quotes or brackets. Their meaning can be found below:
 - `[DNC]` \- Any commit with `[DNC]` anywhere in the message does not compile. This may be for various reasons, including incomplete refactoring or committing changes that accidentally included future definitions of classes/methods/fields.
 - `(<type> <number> [options])` \- Any commit with this pattern somewhere in the commit represents a commit based on a Tutorial episode, with `<number>` being the episode number.
    - `<type>` represents the tutorial series this commit was a part of. Possible values being: 
       - `TUT` \- The main tutorial series
       - `Water` \- A tutorial series from the same channel, about one possible way to make 3D water
       - `Audio` \- A tutorial series from the same channel, about OpenAL and 3D audio
    - `[options]` can be `PART <number>` to represent one of many commits for the same tutorial series and episode or `CODE <number>` to represent of of many commits where actual code for the episode is committed
