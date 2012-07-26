#define scrBegin         static int scrLine = 0; switch(scrLine) { case 0:;
#define scrFinish(z)     } return (z)
#define scrFinishV       } return

#define scrReturn(z)     \
        do {\
            scrLine=__LINE__;\
            return (z); case __LINE__:;\
        } while (0)
#define scrReturnV       \
        do {\
            scrLine=__LINE__;\
            return; case __LINE__:;\
        } while (0)