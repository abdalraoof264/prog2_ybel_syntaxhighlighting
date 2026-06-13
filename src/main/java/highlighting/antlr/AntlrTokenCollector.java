package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.MiniJavaColours;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.*;

public class AntlrTokenCollector extends SyntaxHighlighter {

  @Override
  public List<HighlightRegion> collectMatches(String text) {
    MiniJavaLexer lexer = new MiniJavaLexer(CharStreams.fromString(text));

    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    tokenStream.fill();

    List<Token> tokens = tokenStream.getTokens();
    List<HighlightRegion> regions = new ArrayList<>();

    for (int i = 0; i < tokens.size(); i++) {
      Token token = tokens.get(i);

      if (token.getType() == Token.EOF) {
        continue;
      }

      Color color = getColor(token.getType());

      if (color != null) {
        int start = token.getStartIndex();
        int end = token.getStopIndex() + 1;

        regions.add(new HighlightRegion(start, end, color));
      }

      if (token.getType() == MiniJavaLexer.AT && i + 1 < tokens.size()) {
        Token next = tokens.get(i + 1);

        if (next.getType() == MiniJavaLexer.IDENTIFIER) {
          int start = next.getStartIndex();
          int end = next.getStopIndex() + 1;

          regions.add(new HighlightRegion(start, end, MiniJavaColours.ANNOTATION_COLOUR));
        }
      }
    }

    return regions;
  }

  private Color getColor(int type) {
    if (type == MiniJavaLexer.STRING_LITERAL) {
      return MiniJavaColours.STRING_LITERAL_COLOUR;
    }

    if (type == MiniJavaLexer.CHAR_LITERAL) {
      return MiniJavaColours.CHAR_LITERAL_COLOUR;
    }

    if (type == MiniJavaLexer.LINE_COMMENT) {
      return MiniJavaColours.LINE_COMMENT_COLOUR;
    }

    if (type == MiniJavaLexer.BLOCK_COMMENT) {
      return MiniJavaColours.BLOCK_COMMENT_COLOUR;
    }

    if (type == MiniJavaLexer.JAVADOC_COMMENT) {
      return MiniJavaColours.JAVADOC_COMMENT_COLOUR;
    }

    if (type == MiniJavaLexer.AT) {
      return MiniJavaColours.ANNOTATION_COLOUR;
    }

    if (isKeyword(type)) {
      return MiniJavaColours.KEYWORD_COLOUR;
    }

    return null;
  }

  private boolean isKeyword(int type) {
    return type == MiniJavaLexer.PACKAGE
        || type == MiniJavaLexer.IMPORT
        || type == MiniJavaLexer.CLASS
        || type == MiniJavaLexer.PUBLIC
        || type == MiniJavaLexer.PRIVATE
        || type == MiniJavaLexer.FINAL
        || type == MiniJavaLexer.RETURN
        || type == MiniJavaLexer.NULL
        || type == MiniJavaLexer.NEW
        || type == MiniJavaLexer.IF
        || type == MiniJavaLexer.ELSE
        || type == MiniJavaLexer.WHILE
        || type == MiniJavaLexer.EXTENDS
        || type == MiniJavaLexer.IMPLEMENTS;
  }
}
