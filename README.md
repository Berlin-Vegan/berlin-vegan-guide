# Berlin Vegan Guide
mobile app for android

#build & install

    gradlew installDebug

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