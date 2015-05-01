# PlayScalaAndroid
Play framework backend to the ScalaAndroid app written in Scala.

### Query
```
    http://playscalaandroid.herokuapp.com/info?car=swift
```

### Response
```json
    {"name":"swift","url":"http://i.ndtvimg.com/auto/makers/29/205/suzuki-swift-2011-front-view.jpg"}
```

### Query

```
    http://playscalaandroid.herokuapp.com/info?car=blah
```

### Response

```
    {"name":"not_found","url":"not_found"}
```

## Architecture

![Play Architecture](https://raw.githubusercontent.com/pamu/PlayScalaAndroid/master/images/play.png)

