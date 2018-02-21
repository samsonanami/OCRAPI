Oracle - Changelog
==================

Unreleased
----------
(2018-02-20 09:47:49 +0530	) ignoring fields not included in the key data file
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2018-01-08 11:11:56 +0530	) Abbyy no credit error handling implementation
 Abbyy throws null when there is not enough credit in the given account.
In such a case all the fields will be filled with empty values.

 (Sasitha Gunadasa)(sasitha@Sasitha-G.Zone24x7.lk)
(2017-08-22 16:09:24 +0530	) extracting image processing failure messages from local processing.
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-07-19 10:18:13 +0530	) Merge branch 'develop' into 'master'
 Develop



See merge request !13
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-06-27 11:05:44 +0530	) roi configurations detecting based on template name extracted from local image processing
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-06-23 17:34:00 +0530	) implemented local result extractor
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-06-23 12:38:32 +0530	) updating ocr process status
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-06-23 00:08:03 +0530	) Saving abbyy results in to the database
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-06-22 10:54:40 +0530	) ABBYY connector implementation.
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)

1.0.2 (2017-07-19 10:18:13 +0530)
---------------------------------
(2017-07-19 10:18:13 +0530	) Merge branch 'develop' into 'master'
 Develop



See merge request !13
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-06-20 11:23:55 +0530	) Merge branch 'handle_empty_ocr_extraction_values' into 'develop'
 Handle empty ocr extraction values



See merge request !12
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-06-20 11:23:05 +0530	) Merge branch 'develop' of git.zone24x7.lk:IDAPI/OCRAPI into handle_empty_ocr_extraction_values
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-06-20 11:22:40 +0530	) add Apollo agent deployment scripts
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-01-27 18:22:05 +0530	) Merge branch 'handle_empty_ocr_extraction_values' into 'develop'
 handle empty results value

handling empty and invalid ocr extraction value and ocr
cofidence values return by the native library

See merge request !9
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-01-27 18:20:02 +0530	) handle empty results value
 handling empty and invalid ocr extraction value and ocr
cofidence values return by the native library

 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-01-27 18:09:58 +0530	) Merge branch 'handle-low-confidence-ocr-values' into 'develop'
 validate minimum acceptable ocr confidence level

for each ocr extraction field in an image a minimum acceptable ocr
confidence value can be configured int the resource_name_ocr_extraction_field table.
if the ocr extration confidence value is lower than this value ocr process status
will be set to fail. To skip this validation keep the database value as null

See merge request !8
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-01-27 18:05:33 +0530	) validate minimum acceptable ocr confidence level
 for each ocr extraction field in an image a minimum acceptable ocr
confidence value can be configured int the resource_name_ocr_extraction_field table.
if the ocr extration confidence value is lower than this value ocr process status
will be set to fail. To skip this validation keep the database value as null

 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-01-18 10:48:01 +0530	) [ci-skip] updated changelog
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)

1.0.1-0.0.1 (2017-01-18 10:45:12 +0530)
---------------------------------------
(2017-01-18 10:45:12 +0530	) Fixed compilation warning on using args in main method
 Fixed warning "warning: non-varargs call of varargs method with inexact argument type for last parameter"

 (Sasitha Gunadasa)(sasithag@zone24x7.com)

1.0.1-0.0.0 (2017-01-18 10:39:50 +0530)
---------------------------------------
(2017-01-18 10:39:50 +0530	) Sonar defect fixes
 Sonar defect fixes in all modules.

 (Sasitha Gunadasa)(sasithag@zone24x7.com)
(2017-01-16 13:15:22 +0530	) [ci-skip] added change log files
 
 (Sasitha Gunadasa)(sasithag@zone24x7.com)
