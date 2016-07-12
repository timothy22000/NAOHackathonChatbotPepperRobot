#!/bin/bash

request=`curl -v -XPOST "https://demo.pandorabots.com/talk/mitsuku/mitsukudemo?user_key=pb3568993377180953528873199695415106305&input=$1"`

echo $request