import java.util.ArrayList;
import java.util.Random;

public class BoggleGame implements BoggleGameInterface{

    @Override
    public char[][] generateBoggleBoard(int size) {
        if(size <= 0){
            return null;
        }
        int stringLength = size*size;
        if(stringLength <= 0){
            return null;
        }
        String s = generateRandomString(stringLength);
        char[][] board = new char[size][size];
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                board[i][j] = s.charAt(i*size+j);
            }
        }
        return board;
    }

    @Override
    public int countWords(char[][] boggleBoard, DictInterface dictionary) {
        int count = 0;
        //iterates through each tile of boggleBoard
        for (int i = 0; i < boggleBoard.length; i++) {
            for (int j = 0; j < boggleBoard[i].length; j++) {
                boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[i].length];
                //marks the current tile as 'visited'
                visited[i][j] = true;
                String str = "" + boggleBoard[i][j];
                //this if statement checks if the current string forms a valid prefix by checking the dictionary
                if (dictionary.searchPrefix(new StringBuilder(str)) != 1) {
                    visited[i][j] = false;
                } else {
                  //this if statement checks if the current string is a valid word
                    if (dictionary.searchPrefix(new StringBuilder(str)) == 2) {
                        count++;
                    }
                    //calls the helper method which explores all possible words that can be formed
                    countWordsHelper(boggleBoard, visited, i, j, str, dictionary, count);
                }
            }
        }
        return count;
}


    private void countWordsHelper(char[][] boggleBoard, boolean[][] visited, int i, int j, String str, DictInterface dictionary, int count) {
        visited[i][j] = true;
        str += boggleBoard[i][j];
        //this if statement checks if the current string forms a valid prefix by checking the dictionary
        if (dictionary.searchPrefix(new StringBuilder(str)) != 1) {
          visited[i][j] = false;
          return;
        }
        //increments the count variable if the current string is a valid word
        if (dictionary.searchPrefix(new StringBuilder(str)) == 2) {
          count++;
        }
        //these nested loops explore all possible adjacent positions to form valid words
       for (int row = i - 1; row <= i + 1 && row < boggleBoard.length; row++) {
         for (int col = j - 1; col <= j + 1 && col < boggleBoard[row].length; col++) {
           if (row >= 0 && col >= 0 && !visited[row][col]) {
            countWordsHelper(boggleBoard, visited, row, col, str, dictionary, count);
            }
          }
        }
        //backtracks and marks the current tile as unvisited
        str = "" + str.charAt(str.length() - 1);
        visited[i][j] = false;
      }

    @Override
    public int countWordsOfCertainLength(char[][] boggleBoard, DictInterface dictionary, int wordLength) {
        int count = 0;
   for (int i = 0; i < boggleBoard.length; i++) {
       for (int j = 0; j < boggleBoard[i].length; j++) {
           boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[i].length];
           visited[i][j] = true;
           String str = "" + boggleBoard[i][j];
           if (str.length() == wordLength && dictionary.searchPrefix(new StringBuilder(str)) == 2) {
               count++;
           }
           for (int row = i - 1; row <= i + 1 && row < boggleBoard.length; row++) {
               for (int col = j - 1; col <= j + 1 && col < boggleBoard[row].length; col++) {
                   if (row >= 0 && col >= 0 && !visited[row][col]) {
                       countWordsOfCertainLengthHelper(boggleBoard, visited, row, col, str, dictionary, count, wordLength);
                   }
               }
           }
       }
   }
   return count;
    }

    private void countWordsOfCertainLengthHelper(char[][] boggleBoard, boolean[][] visited, int i, int j, String str, DictInterface dictionary, int count, int wordLength) {
        visited[i][j] = true;
        str += boggleBoard[i][j];
        if (str.length() == wordLength && dictionary.searchPrefix(new StringBuilder(str)) == 2) {
            count++;
        }
        if (str.length() >= wordLength) {
            visited[i][j] = false;
            return;
        }
        for (int row = i - 1; row <= i + 1 && row < boggleBoard.length; row++) {
            for (int col = j - 1; col <= j + 1 && col < boggleBoard[row].length; col++) {
                if (row >= 0 && col >= 0 && !visited[row][col]) {
                    countWordsOfCertainLengthHelper(boggleBoard, visited, row, col, str, dictionary, count, wordLength);
                }
            }
        }
        str = str.substring(0, str.length() - 1);
        visited[i][j] = false;
    }

    @Override
    public boolean isWordInDictionary(DictInterface dictionary, String word) {
      return dictionary.searchPrefix(new StringBuilder(word)) == 2;
    }

    @Override
    public boolean isWordInBoard(char[][] boggleBoard, String word) {
        for (int i = 0; i < boggleBoard.length; i++) {
        for (int j = 0; j < boggleBoard[i].length; j++) {
            if (boggleBoard[i][j] == word.charAt(0)) {
                boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[i].length];
                if (isWordInBoardHelper(boggleBoard, visited, i, j, word, 0)) {
                    return true;
                }
            }
        }
    }
    return false;
    }

    private boolean isWordInBoardHelper(char[][] boggleBoard, boolean[][] visited, int i, int j, String word, int index) {
        if (index == word.length()) {
            return true;
        }
        if (i < 0 || i >= boggleBoard.length || j < 0 || j >= boggleBoard[i].length || visited[i][j] || boggleBoard[i][j] != word.charAt(index)) {
            return false;
        }
        visited[i][j] = true;
        if (isWordInBoardHelper(boggleBoard, visited, i + 1, j, word, index + 1) ||
            isWordInBoardHelper(boggleBoard, visited, i - 1, j, word, index + 1) ||
            isWordInBoardHelper(boggleBoard, visited, i, j + 1, word, index + 1) ||
            isWordInBoardHelper(boggleBoard, visited, i, j - 1, word, index + 1) ||
            isWordInBoardHelper(boggleBoard, visited, i + 1, j + 1, word, index + 1) ||
            isWordInBoardHelper(boggleBoard, visited, i + 1, j - 1, word, index + 1) ||
            isWordInBoardHelper(boggleBoard, visited, i - 1, j + 1, word, index + 1) ||
            isWordInBoardHelper(boggleBoard, visited, i - 1, j - 1, word, index + 1)) {
            return true;
        }
        visited[i][j] = false;
        return false;
    }




    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary) {
      for (int i = 0; i < boggleBoard.length; i++) {
      for (int j = 0; j < boggleBoard[i].length; j++) {
          boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[i].length];
          StringBuilder word = new StringBuilder();
          findAnyWordHelper(boggleBoard, visited, i, j, dictionary, word);
          if (word.length() >= 3) {
              return word.toString();
          }
      }
  }
  return null;
    }



    private void findAnyWordHelper(char[][] boggleBoard, boolean[][] visited, int i, int j, DictInterface dictionary, StringBuilder word) {
    if (i < 0 || i >= boggleBoard.length || j < 0 || j >= boggleBoard[i].length || visited[i][j]) {
        return;
    }
    visited[i][j] = true;
    word.append(boggleBoard[i][j]);
    if (word.length() >= 3 && dictionary.searchPrefix(word) == 2) {
        return;
    }

    findAnyWordHelper(boggleBoard, visited, i + 1, j, dictionary, word);
    findAnyWordHelper(boggleBoard, visited, i - 1, j, dictionary, word);
    findAnyWordHelper(boggleBoard, visited, i, j + 1, dictionary, word);
    findAnyWordHelper(boggleBoard, visited, i, j - 1, dictionary, word);
    findAnyWordHelper(boggleBoard, visited, i + 1, j + 1, dictionary, word);
    findAnyWordHelper(boggleBoard, visited, i + 1, j - 1, dictionary, word);
    findAnyWordHelper(boggleBoard, visited, i - 1, j + 1, dictionary, word);
    findAnyWordHelper(boggleBoard, visited, i - 1, j - 1, dictionary, word);

    visited[i][j] = false;
    word.deleteCharAt(word.length() - 1);
}


    @Override
    public ArrayList<Tile> markWordInBoard(char[][] boggleBoard, String word) {
      ArrayList<Tile> tiles = new ArrayList<>();
  for (int i = 0; i < boggleBoard.length; i++) {
      for (int j = 0; j < boggleBoard[i].length; j++) {
          if (boggleBoard[i][j] == word.charAt(0)) {
              boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[i].length];
              if (markWordInBoardHelper(boggleBoard, visited, i, j, word, 0, tiles)) {
                  return tiles;
              }
          }
      }
  }
  return new ArrayList<>();
    }


    private boolean markWordInBoardHelper(char[][] boggleBoard, boolean[][] visited, int i, int j, String word, int index, ArrayList<Tile> tiles) {
    if (index == word.length()) {
        return true;
    }
    if (i < 0 || i >= boggleBoard.length || j < 0 || j >= boggleBoard[i].length || visited[i][j] || boggleBoard[i][j] != word.charAt(index)) {
        return false;
    }
    visited[i][j] = true;
    tiles.add(new Tile(i, j));
    if (markWordInBoardHelper(boggleBoard, visited, i + 1, j, word, index + 1, tiles) ||
        markWordInBoardHelper(boggleBoard, visited, i - 1, j, word, index + 1, tiles) ||
        markWordInBoardHelper(boggleBoard, visited, i, j + 1, word, index + 1, tiles) ||
        markWordInBoardHelper(boggleBoard, visited, i, j - 1, word, index + 1, tiles) ||
        markWordInBoardHelper(boggleBoard, visited, i + 1, j + 1, word, index + 1, tiles) ||
        markWordInBoardHelper(boggleBoard, visited, i + 1, j - 1, word, index + 1, tiles) ||
        markWordInBoardHelper(boggleBoard, visited, i - 1, j + 1, word, index + 1, tiles) ||
        markWordInBoardHelper(boggleBoard, visited, i - 1, j - 1, word, index + 1, tiles)) {
        return true;
    }
    tiles.remove(tiles.size() - 1);
    visited[i][j] = false;
    return false;
}

    @Override
    public boolean checkTiles(char[][] boggleBoard, ArrayList<Tile> tiles, String word) {
      if (tiles.size() < 3) {
        return false;  // Minimum word length not met
    }
    StringBuilder wordFromTiles = new StringBuilder();
    for (Tile tile : tiles) {
        wordFromTiles.append(boggleBoard[tile.row][tile.col]);
    }
    return wordFromTiles.toString().equals(word);
    }

    @Override
    public String anyWord(char[][] boggleBoard, DictInterface dictionary, int length) {
      for (int i = 0; i < boggleBoard.length; i++) {
        for (int j = 0; j < boggleBoard[i].length; j++) {
            boolean[][] visited = new boolean[boggleBoard.length][boggleBoard[i].length];
            StringBuilder word = new StringBuilder();
            findAnyWordOfLengthHelper(boggleBoard, visited, i, j, dictionary, word, length);
            if (word.length() == length) {
                return word.toString();
            }
        }
    }
    return null;
   }


   private void findAnyWordOfLengthHelper(char[][] boggleBoard, boolean[][] visited, int i, int j, DictInterface dictionary, StringBuilder word, int length) {
    if (i < 0 || i >= boggleBoard.length || j < 0 || j >= boggleBoard[i].length || visited[i][j]) {
        return;
    }
    visited[i][j] = true;
    word.append(boggleBoard[i][j]);
    if (word.length() == length && dictionary.searchPrefix(word) == 2) {
        return;
    }

    findAnyWordOfLengthHelper(boggleBoard, visited, i + 1, j, dictionary, word, length);
    findAnyWordOfLengthHelper(boggleBoard, visited, i - 1, j, dictionary, word, length);
    findAnyWordOfLengthHelper(boggleBoard, visited, i, j + 1, dictionary, word, length);
    findAnyWordOfLengthHelper(boggleBoard, visited, i, j - 1, dictionary, word, length);
    findAnyWordOfLengthHelper(boggleBoard, visited, i + 1, j + 1, dictionary, word, length);
    findAnyWordOfLengthHelper(boggleBoard, visited, i + 1, j - 1, dictionary, word, length);
    findAnyWordOfLengthHelper(boggleBoard, visited, i - 1, j + 1, dictionary, word, length);
    findAnyWordOfLengthHelper(boggleBoard, visited, i - 1, j - 1, dictionary, word, length);

    visited[i][j] = false;
    word.deleteCharAt(word.length() - 1);
}



    private String generateRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();

        // System.out.println(generatedString);
        return generatedString;
    }

}
