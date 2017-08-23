import shutil
import os

source = 'app/build/outputs/apk/app-debug.apk'
dest1 = '/Users/osvaldo/Desktop/app-debug.apk'

shutil.move(source, dest1)
