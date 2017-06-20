Oracle - Changelog
==================

Unreleased
----------
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
