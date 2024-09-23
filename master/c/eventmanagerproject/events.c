/**************************************************************
Reads and displays information from a file about
given events. This includes displaying each event in a pretty
format, displaying events between certain dates, and counting
the amount of certain events in the given set.
***************************************************************/

#include <stdio.h>
#include <string.h>

typedef struct {
	int hour;
	int minute;
} event_time_t;

typedef struct {
	int month;
	int day;
	int year;
} event_date_t;

typedef struct {
	char title[20];
	event_time_t time;
	event_date_t date;
} event;

void make_events(char filename[32], int number_of_events, event events[number_of_events]);
void print_event(event given_event);
void print_selected_events(int number_events, event events[number_events], event_date_t start_date, event_date_t end_date);
void count_events(int number_events, event events[number_events]);

int main() {
	
	char		file[32];
	int 		numev;
	
	// Prompt user for name of input file and # of events
	printf("Event Database:\n\n");
	printf("Enter the name of the input file: ");
	scanf("%s", &file);
	printf("How many events are in the input file? ");
	scanf(" %d", &numev);
	
	event		list_of_events[numev];
	
	// Call make_events, retrieve completed array
	
	make_events(file, numev, list_of_events);
	
	// For each event, use print_event
	printf("\nSchedule of Events:\n\n");
	for (int i = 0; i < numev; i++) {
		print_event(list_of_events[i]);
	}
	
	// Use print_selected_events for events between 01/01/2018 and 12/21/2019
	
	event_date_t		start;
	event_date_t		end;
	start.day = 1;
	start.month = 1;
	start.year = 2018;
	end.day = 20;
	end.month = 12;
	end.year = 2019;
	print_selected_events(numev, list_of_events, start, end);
	
	// Call count_events and print how many times each event appears in the array
	
	count_events(numev, list_of_events);
	
	return 0;
}

// Reads in a file and returns it in array form
void make_events(char filename[32], int number_of_events, event events[number_of_events]) {
	FILE *eventfile_p;
	eventfile_p = fopen(filename, "r");
	
	if (eventfile_p == NULL) {
		printf("An error has occurred while opening the file.");
	}
	
	for (int i = 0; i < number_of_events; i++) {
		fscanf(eventfile_p, " %s ", events[i].title);
		fscanf(eventfile_p, " %d %d ", &events[i].time.hour, &events[i].time.minute);
		fscanf(eventfile_p, " %d %d %d ", &events[i].date.month, &events[i].date.day, &events[i].date.year);
	}
	fclose(eventfile_p);
}

// Prints the given event in a pretty format
void print_event(event given_event) {
	printf("\t\t%s at:\t\t", given_event.title);
	
	if (given_event.time.hour < 10) {
		printf("0%d:", given_event.time.hour);
	} // If the hour is single digit
	else {
		printf("%d:", given_event.time.hour);
	}
	
	
	if (given_event.time.minute < 10) {
		printf("0%d on: ", given_event.time.minute);
	} // If the minute is single digit
	else {
		printf("%d on: ", given_event.time.minute);
	}
	
	
	if (given_event.date.month < 10) {
		printf("0%d/", given_event.date.month);
	} // If the month is single digit
	else {
		printf("%d/", given_event.date.month);
	}
	
	
	if (given_event.date.day < 10) {
		printf("0%d/", given_event.date.day);
	} // If the day is single digit
	else {
		printf("%d/", given_event.date.day);
	}
	
	printf("%d\n", given_event.date.year);
}

// Prints events between two given dates
void print_selected_events(int number_events, event events[number_events], event_date_t start_date, event_date_t end_date) {
	
	printf("\n\nDate:\t01/01/2018\t\t12/20/2019\n\nEvents:\n\n");
	for (int i = 0; i < number_events; i++) {
		if (events[i].date.month >= start_date.month) {
			if (events[i].date.month <= end_date.month) {
				if (events[i].date.day >= start_date.day) {
					if (events[i].date.day  <= end_date.day) {
						if (events[i].date.year >= start_date.year) {
							if (events[i].date.year <= end_date.year) {
								print_event(events[i]);
							} // If the event is before the end date's year
						} //	 "  "   "     "  "      the start date's year
					} // 		 "  "   "     "  "      the end date's day
				} // 			 "  "   "     "  "      the start date's day
			} // 				 "  "   "     "  "      the end date's month
		} // 					 "  "   "     "  "      the start date's month
	} // 						 For every event
}

// Counts the number of each event in a given event array
void count_events(int number_events, event events[number_events]) {
	enum eventnames {
		Birthday,
		Wedding,
		Anniversary,
		Seminar
	};
	enum eventnames testevent;
	int			sum = 0;
	
	printf("\n\nEvents:\n\n");
	printf("\t\tBirthday\t");
	testevent = Birthday;
	for (int i = 0; i < number_events; i++) {
		if (strcmp(events[i].title, "Birthday") == 0) {
			sum++;
		} // If the event is named "Birthday"
	} // 	 For every event
	printf("%d\n", sum);
	sum = 0;
	
	printf("\t\tWedding\t\t");
	testevent = Wedding;
	for (int i = 0; i < number_events; i++) {
		if (strcmp(events[i].title, "Wedding") == 0) {
			sum++;
		} // If the event is named "Wedding"
	} // 	 For every event
	printf("%d\n", sum);
	sum = 0;
	
	printf("\t\tAnniversary\t");
	testevent = Anniversary;
	for (int i = 0; i < number_events; i++) {
		if (strcmp(events[i].title, "Anniversary") == 0) {
			sum++;
		} // If the event is named "Anniversary"
	} // 	 For every event
	printf("%d\n", sum);
	sum = 0;
	
	printf("\t\tSeminar\t\t");
	testevent = Seminar;
	for (int i = 0; i < number_events; i++) {
		if (strcmp(events[i].title, "Seminar") == 0) {
			sum++;
		} // If the event is named "Seminar"
	} // 	 For every event
	printf("%d\n", sum);
	sum = 0;
}