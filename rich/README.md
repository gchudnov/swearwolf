# swearwolf / rich

> Print rich text in the terminal

Allows using tags to specify styles text should be rendered with.

An example of usage:

```scala
val rich = RichText("<b>BOLD</b><fg='#AA0000'><bg='#00FF00'>NOR</bg></fg>MAL<i>italic</i><k>BLINK</k>\n")
```

Produces the following output:

![rich-test](../res/images/rich.png)

## HowTo

`RichText` can be constructed using the following html-like tags:

- **bold**, **b**

  ```html
  <bold>TEXT</bold>
  <b>TEXT</b>
  ```

- **italic**, **i**

  ```html
  <italic>TEXT</italic>
  <i>TEXT</i>
  ```

- **underline**, **u**

  ```html
  <underline>TEXT</underline>
  <u>TEXT</u>
  ```

- **blink**, **k**

  ```html
  <blink>TEXT</blink>
  <k>TEXT</k>
  ```

- **invert**, **v**

  ```html
  <invert>TEXT</invert>
  <v>TEXT</v>
  ```

- **strikethrough**, **t**

  ```html
  <strikethrough>TEXT</strikethrough>
  <t>TEXT</t>
  ```

- **fg**, **fgcolor**, **color**

  ```html
  <fg='#FF0000'>TEXT</fg>
  <fgcolor='#FF0000'>TEXT</fgcolor>
  <color='#FF0000'>TEXT</color>
  ```

- **bg**, **bgcolor**, **background**

  ```html
  <bg='#00FF00'>TEXT</bg>
  <bgcolor='#00FF00'>TEXT</bgcolor>
  <background='#00FF00'>TEXT</background>
  ```

Quotes could be either singular (`'`) or double (`"`).

## Examples

TODO: point to the examples
