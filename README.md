# SimpleChess — Multiplayer

## Introduction  

This project, **SimpleChess**, is a Pure Java chess application that demonstrates how to design and implement a network-enabled board game using fundamental object-oriented programming concepts. The application supports **local play**, where two players share the same computer, as well as **multiplayer mode** using TCP sockets, where one player hosts the server and another joins as the client. The program follows the standard rules of chess, including castling, pawn promotion, check, checkmate, and stalemate detection. 

The development of this chess application serves as both a programming exercise and a case study in integrating **user interface programming, networking, and game logic** within a single project. By using pure Java, without external libraries, the project remains lightweight and easily portable across systems that have Java Runtime Environment (JRE) installed.  

## Features  

The program includes the following main features:  

1. **Local Mode**: Two players can share the same computer, alternating turns. White pieces are always at the bottom in local play for familiarity.  
2. **Network Play**:  
   - The host plays as White and starts the game.  
   - The client connects via IP and port and plays as Black.  
   - The user interface flips automatically so each player always sees their own color at the bottom.  
3. **Legal Move Validation**: All moves are validated against the rules of chess. The interface highlights legal destinations when a piece is selected (green squares for normal moves, red squares for captures).  
4. **Special Rules**:  
   - Castling is supported with full safety checks.  
   - Pawn promotion allows choosing a Queen, Rook, Bishop, or Knight.  
   - The system detects check, checkmate, and stalemate, **alerting the players with pop-up dialogs**.  
5. **User Interface**: The pieces are displayed using Unicode symbols (♔ ♕ ♖ ♗ ♘ ♙ for White and ♚ ♛ ♜ ♝ ♞ ♟ for Black). This avoids the need for external images while ensuring clear visual representation.  
6. **Move History**: A sidebar displays the algebraic notation of moves, allowing players to follow the game progression.  

## Design and Implementation  

The program is organized into modular classes:  

- **Piece Hierarchy**: Abstract `Piece` class with subclasses (`King`, `Queen`, `Rook`, `Bishop`, `Knight`, `Pawn`). Each subclass implements movement logic and provides its Unicode symbol.  
- **Board Class**: Manages the 8×8 grid, tracks game state, generates legal moves, applies moves, and checks for special conditions such as checkmate.  
- **Move Class**: Represents a chess move with source and destination coordinates, as well as special flags (e.g., castling).  
- **UI Class**: Handles the graphical interface using Swing. The board is drawn with `JButton` components, and moves are executed through click events.  
- **Networking Classes**: `TcpServer` and `TcpClient` extend a shared `TcpBase` to provide a lightweight protocol. Messages encode moves and promotion choices, ensuring both sides remain synchronized.  

This separation of concerns makes the system easy to understand, extend, and debug.  

## Networking Model  

The application uses a **client–server model** over TCP. The host listens on a port (default 5000) and accepts a connection from a single client. Moves are serialized as text messages, for example:  

- `"M 6 4 4 4"` → a move from row 6, column 4 to row 4, column 4 (e2–e4).  
- `"P Queen"` → a promotion request.  

This lightweight protocol avoids complexity while being robust enough for two-player communication.  

To ensure correctness, moves are always transmitted before promotion decisions. If a promotion arrives early due to network conditions, the client queues it until the corresponding move is applied.  

## Limitations  

The program omits some advanced chess features, such as en passant and draw detection by repetition or the fifty-move rule. These exclusions are intentional to keep the codebase clean and focused on core educational goals. The interface is also minimalistic, prioritizing clarity over visual effects.  

## UML Diagram Link
[UML Diagram](https://mermaid.live/view#pako:eNq9V1tTGzcU_is7y2TGHmyXSw1mhzJju2lIgdbBbpkp5kHePfaq7EqupOUSQn57j6S9aLFNwksYwNK56dy-I_nJD3kEfuCHCZHyV0oWgqRTFlEBoaKceZPBlE3Zu3feLy9_vCEX4F2gerLKnDJjEGUSLsY0Au9pyjzv-BhYlp6c6PX21enHyXuzGpz3h2dmxZdLLqmCRrNSnbJn7YM1OKIQlsbITCpBQpUbrA4L9crQZpwnQJiX8juIDCUk7AI3jQEnImp5et30crFCoK8UCW83imwtiYqHuBO5CGXK_XOEHdfPKFtYz7_hw3Oh8SkDYG9TueT89m0aAypjvnybzhmji1i9TWdE7r8vlu8ogcmqbYXjL-22SW2NYDJXo-jE1Ag27roZE1aNpL2uSqjdyGPAMntzwdPL2m5Y7hS_dNbDWjOGRKoEDAn-y0giG3_O_kXA1ZNgWVdUxb8lZCEbq2mKiYyHCEBECx5TazaTN-tq24RzfXN94y3MvnDjPkakTbi2u0rXKb0oQFOyZgkWZT3LaPV1mjfwTtfzjMn-K7y63rYtzhKTzfWEGgFOK2YC216AapQQNHKGLCtyy1Cb3h2nUV6exipoNV1eVdlp1Hloz2XmLMeoAH2kQ1Di8YLcghFfnSZkuUweHV6pZoME3YMN43hrrAQGW_N_GAPipJx9L8Mw7JSol0FQOVYkgTWcBBYk0Z5I1-o5leqrpn4t1LE9BViYQlQmeL0jW3PKIt03rkkUv74xXEnm0J8rEGUWNtjZJskCZoLQ0EqZNm96Ni05AGzrt9sn3tTvPfSmfn5pBHgtMEUok66MgXTgZRJq5OouKXjWyEseQnDjBfnXR--cPIJ45XqMQUoUszi1p8_0f7P_fZApxdl17wZ_PWnyLS1nAg-qL4B4MZaFi0dLPSczvIylIirL5UaEIWUhaGRWhqgnkoTk0t0MV2GH7SGUNfMHqCFnLH8SMFA1adpPDRzWTAM14BhBam9nG2ujZqxVFLdptBncfyApONBpY8jakXMeksSlc2YbsIJvyYpxiid6ko8hscf0WWQbelU41Nf4aaEh3SOyZYToMFVZJY9Nll16qJH2nkWaAzXOHYV7PSzwqVR5UHZ_O9WMCf8bpdawt_WMc5DhgN9yRsUobKxMh1wVE08WsIn9KaPVsHp2L7sIRhTBLfIrryI0mnVJBFUuk-IyP-j6pma06HWNINvpgZcSRpdZovNVF8hRGWKLI-8ni8BCoNM58eotqaGdphmjoTXluK6Fc0Wka0c1pRLYiF484Z6LW_1o24jfuhf5qxQrB2JOQsifpXgX6OEJDPNWLJwiSLDN-bK4mv5KaY3apsKGCZcuinBc9xNav8ic-hVebYrgx3dgPbEcH2KFj5XXk3A5IHLDl4GtMcfq4mwzH4YyyOZzEBBdAokwVmovphF6pK4ETivh8cxKTmLsu8hT5sNQ-jjFaDjIhxvRuTT0MnNJ6d63K76mGPoO1GPO-tb40e0hMrZuAmCGxyDuisZo202RWbMx6qWcHl_NNUfUbQ4TCqz49lDuczdb1sSaLjj-0ukURZ-yovrmlV46sEq3xg09j8Xi38wXjIFiMT_rmVE5tkHAHT9l3QOPpkt8RqGe9Fu-vmn9YI7vdmj5KQgch7j3TahTX8UoOPUDXEZE3E59jBOVloT9w3nqB0pkqCZ4tohLI_a2yb-ZlyJYXRBDnjHlB11jwQ-e_Ac_2N096Owf7u4fHXa7Ozv73e5ey3_0g_2Dzt7PR0e93f297kF3b7_XfW75n82hO53eIbKcn-f_ARu2has)


## How to Run  

1. Compile the program from the project root:  
   ```bash
   C:\Users\ASUS\Desktop\FinalSimpleChess\SimpleChess
   ```  
2. Compile the program from the project root:  
   ```bash
   javac -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
   ```  
3. Run the launcher:  
   ```bash
   java -cp bin com.globalacademy.finalChess.gui.screens.Main
   ```  
4. Choose **Local**, **Host**, or **Join** from the menu.  
   - Host players provide a port (e.g., 5000).  
   - Clients must enter the host’s IP address and port.  

Ensure that both host and client run from the same WLAN or have proper firewall/port-forwarding settings for external play.  

## Conclusion  

**SimpleChess** is more than just a playable game: it is a demonstration of how traditional board games can be modeled in code through clean abstractions, natural UI design, and practical networking. By combining object-oriented design with user interface programming and TCP communication, the project highlights the versatility of Java for building interactive applications. While simplified, it captures the essence of chess and provides a solid foundation for further enhancements such as timers, player profiles, or AI opponents.  

## License
