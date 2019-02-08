# cache-aware-field-calculator-for-geoevent



##### ArcGIS GeoEvent Extension Cache Aware Field Calculator Processor

This custom processor is similar to the GeoEvent Field Calculator and can access an in-memory cache that stores previous GeoEvents for each track. 
For example, given the "Speed" field on the input GeoEvent Definition, one can compute the value of "IsMoving" field by comparing the current "Speed" value with the previous one using the syntax below:

Expression: previousGeoEvent('Speed') != Speed

The new field "IsMoving" with the type of boolean can be added to the incoming GeoEvent (for the output one) or replacing the existing field if it is available.

The maximum number of tracks in the cache (default to 1000 tracks) is configurable by changing the file <geoevent_install_dir>\com.esri.ges.manager.servicemanager.cfg

com.esri.ges.manager.servicemanager.maxCacheSize=1000

![App](cache-aware-fieldcalculator-for-geoevent.png?raw=true)

## Changes

This fork contains the following function:

`parseDate('<date_string>')` - Converts a string in the format `yyy-MM-dd` into a `Date` object.
`parseDate('<date_string>', <dateFormat>')` - Converts a string to a date based on a custom format string.

## Features
* Cache Aware Field Calculator Processor

## Instructions

Building the source code:

1. Make sure Maven and ArcGIS GeoEvent Extension SDK are installed on your machine.
2. Run 'mvn install -Dcontact.address=[YourContactEmailAddress]'

Installing the built jar files:

1. Copy the *.jar files under the 'target' sub-folder(s) into the [ArcGIS-GeoEvent-Extension-Install-Directory]/deploy folder.

## Requirements

* ArcGIS GeoEvent Extension for Server.
* ArcGIS GeoEvent Extension SDK.
* Java JDK 1.8 or greater.
* Maven.

## Resources

* [ArcGIS GeoEvent Extension for Server Resources](http://links.esri.com/geoevent)
* [ArcGIS Blog](http://blogs.esri.com/esri/arcgis/)
* [twitter@esri](http://twitter.com/esri)

## Issues

Find a bug or want to request a new feature?  Please let us know by submitting an issue.

## Contributing

Esri welcomes contributions from anyone and everyone. Please see our [guidelines for contributing](https://github.com/esri/contributing).

## Licensing
Copyright 2017 Esri

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

A copy of the license is available in the repository's [license.txt](license.txt?raw=true) file.
