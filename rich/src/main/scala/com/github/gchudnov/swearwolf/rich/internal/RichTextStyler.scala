package com.github.gchudnov.swearwolf.rich.internal

import com.github.gchudnov.swearwolf.rich.internal.RichTextParser.{ Attr, Block }
import com.github.gchudnov.swearwolf.util.func.Transform
import com.github.gchudnov.swearwolf.util.colors.Color
import RichTextParser.*
import com.github.gchudnov.swearwolf.rich.RichTextException

/**
 * Processes parsed text and convert blocks and attributes to styles. At this stage all unknown tags and attribute will trigger an error.
 */
private[rich] object RichTextStyler:

  type StyleMapFunc = Seq[RichStyle] => RichStyle

  private val StyleRoot               = "root"
  private val StyleColor              = "color"
  private val StyleFgColorLong        = "fgcolor"
  private val StyleFgColorShort       = "fg"
  private val StyleBgColorLong        = "bgcolor"
  private val StyleBgColorShort       = "bg"
  private val StyleBoldLong           = "bold" // 
  private val StyleBoldShort          = "b"    //
  private val StyleItalicLong         = "italic" //
  private val StyleItalicShort        = "i"      //
  private val StyleUnderlineLong      = "underline"  //
  private val StyleUnderlineShort     = "u"          //
  private val StyleBlinkLong          = "blink"      //
  private val StyleBlinkShort         = "k"          //
  private val StyleInvertLong         = "invert"   //
  private val StyleInvertShort        = "v"          ///
  private val StyleStrikethroughLong  = "strikethrough" //
  private val StyleStrikethroughShort = "t" //

  /**
   * Convert blocks to styles During conversion, check if blocks and attributes are recognized. If not, return an error.
   */
  def style(block: Block): Either[Throwable, RichStyle] =

    def nested(is: Seq[Block]): Either[Throwable, Seq[RichStyle]] =
      Transform.sequence(is.map(it => iterate(it)))

    def iterate(b: Block): Either[Throwable, RichStyle] =
      b match
        case TextBlock(value) =>
          Right[Throwable, RichStyle](RichTextStyle(value))
        case NamedBlockSeq(name, attrs, inner) =>
          name match
            case StyleRoot =>
              nested(inner).map(ss => RichBoxStyle(ss))
            case StyleBoldLong | StyleBoldShort =>
              nested(inner).map(ss => RichBoldStyle(ss))
            case StyleItalicLong | StyleItalicShort =>
              nested(inner).map(ss => RichItalicStyle(ss))
            case StyleUnderlineLong | StyleUnderlineShort =>
              nested(inner).map(ss => RichUnderlineStyle(ss))
            case StyleBlinkLong | StyleBlinkShort =>
              nested(inner).map(ss => RichBlinkStyle(ss))
            case StyleInvertLong | StyleInvertShort =>
              nested(inner).map(ss => RichInvertStyle(ss))
            case StyleStrikethroughLong | StyleStrikethroughShort =>
              nested(inner).map(ss => RichStrikethroughStyle(ss))
            case StyleColor =>
              nested(inner).flatMap { ss =>
                attributes(attrs).map { fs =>
                  val xs = fs.foldRight(ss) { (f, acc) =>
                    Seq(f(acc))
                  }
                  RichBoxStyle(xs)
                }
              }

            case _ =>
              Left[Throwable, RichStyle](new RichTextException(s"Unknown tag in rich-text: '$name'."))

    iterate(block)

  private def attributes(attrs: Seq[Attr]): Either[Throwable, Seq[StyleMapFunc]] =
    attrs.partitionMap {
      case NamedAttr(key, value) =>
        key match
          case StyleFgColorLong | StyleFgColorShort =>
            Color.parse(value).map(c => ((bs: Seq[RichStyle]) => RichForegroundStyle(c, bs)): StyleMapFunc)
          case StyleBgColorLong | StyleBgColorShort =>
            Color.parse(value).map(c => ((bs: Seq[RichStyle]) => RichBackgroundStyle(c, bs)): StyleMapFunc)
          case _ =>
            Left(new RichTextException(s"Unknown attribute in rich-text: '$key'."))
      case _ =>
        Left(new RichTextException(s"Unknown attribute type in rich-text."))
    } match
      case (Nil, rights) => Right[Throwable, Seq[StyleMapFunc]](rights)
      case (lefts, _)    => Left[Throwable, Seq[StyleMapFunc]](lefts.head)

  private[internal] sealed trait RichStyle
  private[internal] final case class RichBoxStyle(inner: Seq[RichStyle])                      extends RichStyle
  private[internal] final case class RichTextStyle (value: String)                             extends RichStyle
  private[internal] final case class RichForegroundStyle (color: Color, inner: Seq[RichStyle]) extends RichStyle
  private[internal] final case class RichBackgroundStyle (color: Color, inner: Seq[RichStyle]) extends RichStyle
  private[internal] final case class RichBoldStyle (inner: Seq[RichStyle])                     extends RichStyle
  private[internal] final case class RichItalicStyle (inner: Seq[RichStyle])                   extends RichStyle
  private[internal] final case class RichUnderlineStyle (inner: Seq[RichStyle])                extends RichStyle
  private[internal] final case class RichBlinkStyle (inner: Seq[RichStyle])                    extends RichStyle
  private[internal] final case class RichInvertStyle (inner: Seq[RichStyle])                   extends RichStyle
  private[internal] final case class RichStrikethroughStyle (inner: Seq[RichStyle])            extends RichStyle
