## Intro 
Imagine you have a commandline app to show tomorrow's forecast using public API: https://www.metaweather.com/api/

Sample output:
```
$ forecast dubai

Tomorrow (2019/05/01) in Dubai:
Clear
Temp: 26.5 °C
Wind: 7.6 mph
Humidity: 61%
```

## Task
* How will you test the app? Write 1-2 automated tests to prove the correct work of application.
* Ideally, tests should not touch the real service and work without the Internet.
* Bonus task. Create CI pipeline with GitHub Actions or any alternative.