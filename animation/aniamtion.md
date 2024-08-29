imageView.setTramslationX（500） 



### ViewPropertyAnimator

```
View.animate().translationX(500);

```

监听器

```
View.animate()
	.translationX(500)
	.setListener(new AnimationStart)
```

### ObjectAnimator

```java
ObjectAnimator animator = ObjectAnimator.ofFloat(View,"translationX",500);
animator.start();
```



```
ObjectAnimator.ofFloat();
ObjectAnimator.ofInt();
ObjectAnimator.ofArgb();
ObjectAniamtor.ofObject();
ObjectAnimator.ofMultiFloat();
ObjectAnimator.ofMultiInt();
ObjectAnimator.ofPropertyValuesHolder();
```

