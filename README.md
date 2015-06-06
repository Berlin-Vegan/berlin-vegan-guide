# Berlin Vegan Guide
mobile app for android

#build & install

    gradlew installDebug

# release creation

* check out master, if not already happened, and make sure you are up-to-date, e.g. `git fetch --all`, `git pull`, etc.
* merge branch development `git merge development`
* set annotated git tag for version, e.g. `TAG=0.1 && git tag -a ${TAG} -m ${TAG}`
* build the release `./gradlew assembleRelease -PsharedSecret=<acra_shared_secret>`
* sign the release, e.g. `jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore my-release-key.keystore ./app/build/outputs/apk/app-release-unsigned.apk alias_name`
* give the app a better name, e.g. `mv ./app/build/outputs/apk/app-release-unsigned.apk ./app/build/outputs/apk/org.berlin_vegan.app_alpha.apk`
* install the release `adb install -r ./app/build/outputs/apk/org.berlin_vegan.app_alpha.apk`
* check is everything is fine and especially check the version string under 'About'
* if everything is fine, push to upstream `git push origin master --tags`

#json format
##gastronomy locations

Fieldname               | Datatype    | Comment
----------------------- | ----------- | -------
id                      | string      | unique id
name                    | string      | location name
street                  | string      | location street address
citycode                | number      | location citycode
city                    | string      | location city
district                | string      | district in the city
latCoord                | double      | gps lat position
longCoord               | double      | gps long position
publicTransport         | string      | public transport direction description
telephone               | string      | telephone number
website                 | string      | webpage URL, without http/s
email                   | string      | email address for location
otMon...otSun           | string      | opening/closing times, format: xx - xx
vegan                   | number      | 1 = omnivore, 2 = omnivore (vegan declared), 3 = vegetarian, 4 = vegetarian (vegan declared), 5 = 100% vegan, Berlin Vegan use only 2,4,5
handicappedAccessible   | number      | handicapped accessible<sup>1</sup>
handicappedAccessibleWc | number      | wc is handicapped accessible<sup>1</sup>
dog                     | number      | dogs allowed<sup>1</sup>
childChair              | number      | child chair available<sup>1</sup>
catering                | number      | catering available<sup>1</sup>
delivery                | number      | delivery available<sup>1</sup>
organic                 | number      | mostly organic food<sup>1</sup>
seatsOutdoor            | number      | number of seats, -1 = unknown
seatsIndoor             | number      | number of seats indoor , -1 = unknown
comment                 | string      | description for location
wlan                    | number      | free wifi available<sup>1</sup>
glutenFree              | number      | gluten free food available<sup>1</sup>
tags                    | stringarray | used for type of location: restaurant, imbiss, eiscafe, cafe

<sup>(1)</sup> 1 = yes, 0 = no, -1 = unknown
