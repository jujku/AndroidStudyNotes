### 位置约束

方向约束，至少对横纵各一个约束。

#### 基本方向约束

```xml
子元素属性
<View
app:layout_constraintLeft_toLeftOf="id or parent"
app:layout_constraintTop_toTopOf="id or parent"
      />

constraint A _to B Of : 当前组件的 A 方向 对齐 指定组件的 B 方向
```

- start-end
- top-bottom
- left-right
- baseline 基线对齐

#### 角度约束

```xml
<V
    app:layout_constraintCircle=""         目标控件id
    app:layout_constraintCircleAngle=""    对于目标的角度(0-360)
    app:layout_constraintCircleRadius=""   到目标中心的距离
```

#### 百分比偏移

偏移维度的，开始和结束都要进行约束才有效果

```xml
<V
    app:layout_constraintHorizontal_bias=""   水平偏移 取值范围是0-1的小数
    app:layout_constraintVertical_bias=""     垂直偏移 取值范围是0-1的小数
```



###  控件内边距,外边距，GONE Margin

内外边距的设置和其他布局一致。

#### GONE Margin

```xml
<V
	app:layout_goneMarginBottom="0dp"
    app:layout_goneMarginEnd="0dp"
    app:layout_goneMarginLeft="0dp"
    app:layout_goneMarginRight="0dp"
    app:layout_goneMarginStart="0dp"
    app:layout_goneMarginTop="0dp"
```

当所依赖的控件，不显示时，这个属性会生效。并且基于所依赖的控件的中心点。



### 控件尺寸

#### 0dp(MATCH_CONSTRAINT)

当某个维度为0dp时，此属性生效

```xml
<V
    app:layout_constraintWidth_default="spread|percent|wrap"
    app:layout_constraintHeight_default="spread|percent|wrap"
```

- spread(默认) 占用所有的符合约束的空间
- percent 按照约束的 百分比

> ​	layout_constraintWidth_percent 设置百分比

- wrap 匹配内容大小但不超过约束限制(如warp-content，文字过多时会无视间距。而此属性就不会超过约束

- 附加属性

  强制约束，可实现和上面wrap一样的效果

  ```XML
  <!--  当一个view的宽或高,设置成wrap_content时  -->
  <V
      app:layout_constrainedWidth="true|false"
      app:layout_constrainedHeight="true|false"
  ```

  

#### 比例宽高

```xml
<V
	app:layout_constraintDimensionRatio=""  宽高比例
```

一个维度为0dp时生效。

两种取值 ” x:x“ "0-1"

### Chains

控件在一个维度，首尾互相约束，形成一条链

三个属性

```xml
 <v
 	app:layout_constraintHorizontal_chainStyle="spread"
```

- spread 平分所有约束空间
- spread-inslde 和spread一样，但首尾空间紧贴父控件
- packed 紧贴在中间，所有控件

 chains还可以设置权重

> `Chains(链)`还支持`weight（权重）`的配置，使用`layout_constraintHorizontal_weight`和`layout_constraintVertical_weight`进行设置链元素的权重
