### 自定义 View 的分类
　　自定义 `View` 的分类标准不唯一，而笔者则把自定义 `View` 分为4类。

  1. 继承 `View` 重写 `onDraw` 方法 <br/>

  　　这种方法主要用于实现一些不规则的效果，即之中效果不方便通过布局的组合方式来达到，往往需要静态或者动态的显示一些不规则的图形。很显然这需要通过绘制的方式来实现，即重写 `onDraw` 方法。采用这种方法需要自己支持 `wrap_content` ，并且 `padding` 也需要自己处理。

  2. 继承 `ViewGroup` 派生特殊的 `Layout`

  　　这种方法主要用于实现自定义的布局， 即除了 `LinearLayout`,`RelativeLayout`,`FrameLayout` 这几种系统的布局之外，我们重新定义一种布局，当某种效果看起来很像几种 `View` 组合在一起的时候，可以采用这种方法来实现。采用摊这种方式稍微复杂一些，需要合适地处理 `ViewGroup` 的测量,布局这两个过程，并同时处理子元素的测量和布局过程。

  3. 继承特定的 `View` (比如 `TextView`)

  　　这种方法比较常见，一般是用于扩展某种已有的 `View` 的功能，比如 `TextView` ，这种方法比较容易实现。这种方法不需要自己支持 `wrap_content` 和 `padding` 等。

  4. 继承特定的 `ViewGroup` (比如 `LinearLayout`)

  　　这种方法也比较常见，当某种效果看起来很像几种 `View` 组合在一起的时候，可以采用这种方法来实现。采用这种方法不需要自己处理 `ViewGroup`　的测量和布局这两个过程。需要注意这种方法和方法2的区别，一般来说方法2能实现的效果方法4也都能实现，两者的主要差别在于方法2更接近 `View` 的底层。　
