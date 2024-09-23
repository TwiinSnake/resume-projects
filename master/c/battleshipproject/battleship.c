/**************************************************************
Plays the popular board game Battleship. The player
has a limited number of guesses to sink the computer's battleships,
and must sink them all to win.
***************************************************************/

#include <stdio.h>
#include <stdlib.h>

void read_map(int height, int length, char map[height][length], char shown_map[height][length], FILE *file);

void print_map(int height, int length, char map[height][length]);

int play_game(int height, int length, char map[height][length], char shown_map[height][length], int shot_limit);

int main(int argc, char *argv[]) {
	
	FILE		*given_map_file;
	int			map_height;
	int			map_length;
	int 		shots;
	int			game_result = 2;
	
	/* Reading in the command line parameters and opening the given file */
	shots = atoi(argv[1]);
	given_map_file = fopen(argv[2], "r");
	fscanf(given_map_file, "%d %d", &map_height, &map_length);
	
	/* Creating the maps to be used for checking shots and for the user to see */
	char		given_map[map_height][map_length];
	char		visible_map[map_height][map_length];
	read_map(map_height, map_length, given_map, visible_map, given_map_file);
	
	/* Beginning the play_game function, returning its result to game_result */
	game_result = play_game(map_height, map_length, given_map, visible_map, shots);
	printf("\n");
	
	if (game_result == 1) {
		printf("\nCaptain, we have sunk all the battleships. You win!\n");
	}												/* If the player won by sinking all the battleships */
	else if (game_result == 0) {
		printf("\nCaptain, we ran out of ammo. You lose!\n");
	}												/* If the player lost by running out of ammo */
	else if (game_result == 2) {
		printf("\nCaptain, the computer is malfunctioning.\n");
		printf("This wasn't a battle you could have won.\n");
	}					/* If the player entered a shot limit lower than the amount of ships on the map */
	
	/* Properly close the file, end the program */
	fclose(given_map_file);
	return 0;
}

/* Fills both maps with ~, then replaces ~ with B in the actual map where they should be */
void read_map(int height, int length, char map[height][length], char shown_map[height][length], FILE *file) {
	char test;
	for (int i = 0; i < height; i++) {
		for (int n = 0; n < length; n++) {
			map[i][n] = '~';
			shown_map[i][n] = '~';
		}
	}
	for (int i = 0; i < height; i++) {
		for (int n = 0; n < length; n++) {
			fscanf(file, " %c ", &test);
			if (test == 'B') {
				map[i][n] = 'B';
			}								/* Only replace ~ if it is a B */
		}
	}
}

/* Simply prints the given map to the screen in a readable way */
void print_map(int height, int length, char map[height][length]) {
	for (int i = 0; i < height; i++) {
		printf("\n");
		for (int n = 0; n < length; n++) {
			printf("%c",map[i][n]);
		}
	}
}

/*
* The play_game function has a lot of tasks to accomplish
* locally to itself, and therefore unfortunately needs to
* take up more than 30 lines of code.
*/

/* Plays the game battleship, with the help of the other two functions. */
int play_game(int height, int length, char map[height][length], char shown_map[height][length], int shot_limit) {
	int		x;
	int		y;
	int		hits = 0;
	int		shots = shot_limit;
	int		ships = 0;
	
	/* For every B found in map, add to ships */
	for (int i = 0; i < height; i++) {
		for (int n = 0; n < length; n++) {
			if (map[i][n] == 'B') {
				ships++;
			}
		}
	}
	
	while ((shots != 0) && (hits != ships)) {
		print_map(height, length, shown_map);
		printf("\n%d shots remaining.\n", shots);
		printf("Captain, please enter coordinates: ");
		scanf("%d %d", &x, &y);
		if ((x > height) || (x < 0)) {
			printf("ERROR: Illegal row input\n");
		}									/* If the input row is out of bounds */
		if ((y > length) || (y < 0)) {
			printf("ERROR: Illegal column input\n");
		}									/* If the input column is out of bounds */
		if ((shown_map[x][y] == 'M') || (shown_map[x][y] == 'H')) {
			printf("ERROR: Already shot in that position\n");
		}									/* If the player already shot in that location */
		else if (map[x][y] == '~') {
			printf("MISS!\n");
			shown_map[x][y] = 'M';
			shots--;
		}									/* If the shot hit the water */
		else if (map[x][y] == 'B') {
			printf("HIT!\n");
			shown_map[x][y] = 'H';
			shots--;
			hits++;
		}									/* If the shot hit a battleship */
	}										/* While the player has shots left, and they haven't hit every ship*/
	
	printf("\nYour shots:");
	print_map(height, length, shown_map);
	printf("\n\nActual map:");
	print_map(height, length, map);
	
	if (hits == ships) {
		return 1;
	} else if (shot_limit < ships) {
		return 2;
	} else {
		return 0;
	}
}