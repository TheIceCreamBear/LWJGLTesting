# LWJGLTesting
A test LWJGL repository, to learn about OpenGL, LWJGL, and GLSL. This project was used to familarize my self with LWJGL and the world of 3D rendering. This project is not open source, as I am the only person who will ever develop it. However, the source code is (*now*) public, allowing viewers to view the madness that is this project.

This readme contains multiple major sections that outline various aspects of the project:
 - Purpose
 - Important Notes
 - Commit message key

## Purpose
The purpose of this project is to learn how to make a 3D rendering engine using OpenGL and by extension LWJGL. It followed a [YouTube tutorial series](https://www.youtube.com/playlist?list=PLRIWtICgwaX0u7Rf9zkZhLoLuZVfUksDP) by [TheThinMatrix on YouTube](https://www.youtube.com/user/ThinMatrix).

The project does include personally desired features, including a couple different rendering scenes. Further, this project did some things differently from the YouTube series, including window management with GLFW.

## Important Notes
This project was originally made as a private repository with little intention to make it public. I used this repository to keep track of my work, be able to work on this in multiple places, and share my progress with friends. Because of this there are some things that need to be considered.

### Comments
Comments were heavily used in this project. They helped me understand what was happening by marking it for what that line did. In fact, this is probably my most commented project. However, because I was developing this privately for personal development and growth, some of the comments are frankly "unprofessional", and may or may not include explicitives, spelling errors, obscure references to popular memes, and other misc rants and raves.

Personally, I find that to not at all be an issue, based on the nature of the project and its original intention. I have no plans to "sanitize" the comments or other aspects of the project, as doing so would, in my eyes, corrupt the nature of this project.

### Code
To be quite frank, some of this code in here is garbage. While there is a consistent and ever present code style based on the accepted Java standards with small modification, there is also a lack of design unity. Some parts are optimized and smartenized, others are plain garbage inefficent code that I dont enjoy looking at.

Part of this is due to the nature of the project, similar to comments, with this project primarily focused on learning about OpenGL and how LWJGL integrates with it rather than writing the cleanest and best Java around. Part of it is also due to me just simply being lazy sometimes when writing personal features outside of the tutorial series.

## Commit message key
Some commit messages contain more than a message. This information is usually enclosed in either quotes or brackets. Their meaning can be found below:
 - `[DNC]` \- Any commit with `[DNC]` anywhere in the message does not compile. This may be for various reasons, including incomplete refactoring or committing changes that accidentally included future definitions of classes/methods/fields.
 - `(<type> <number> [options])` \- Any commit with this pattern somewhere in the commit represents a commit based on a Tutorial episode, with `<number>` being the episode number.
    - `<type>` represents the tutorial series this commit was a part of. Possible values being: 
       - `TUT` \- The main tutorial series
       - `Water` \- A tutorial series from the same channel, about one possible way to make 3D water
       - `Audio` \- A tutorial series from the same channel, about OpenAL and 3D audio
    - `[options]` can be `PART <number>` to represent one of many commits for the same tutorial series and episode or `CODE <number>` to represent of of many commits where actual code for the episode is committed
