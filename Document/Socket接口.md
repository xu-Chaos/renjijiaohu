# Socket接口
### 1.房间信息

发送示例：

roomWidth和roomHeight表示房间的宽和高

furniture数组中每个对象代表一个家具：

id指这个家具的唯一id，classID表示这个家具类的id，对应家具的具体信息见FurnitureClass文档

x和y表示这个家具左上角的坐标，rotate表示方向，如下

![rotate](image\rotate.png)

```json
{
  "type": "room",
  "roomWidth": 10,
  "roomHeight": 15,
  "furniture": [
    {
      "id": 1,
      "classID": 1,
      "x": 5,
      "y": 6,
      "rotate": 1
    },
    {
      "id": 2,
      "classID": 1,
      "x": 1,
      "y": 1,
      "rotate": 1
    }
  ]
}
```



### 2.位置信息

例子：

##### request:

```json
{
    "type": "location"
}
```

##### response:

```json
{
    "x": 0.4569,
    "y": 0.8431,
    "rotate": 75.6
}
```

解释：

![location](image\location.png)
