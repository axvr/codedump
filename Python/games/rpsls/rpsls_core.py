#Rock, Paper, Scissors, Lizard, Spock - Core
#rpsls_core_py3.py for Python 3.4 | Written by Alex Vear
#Written: 19th December 2015 | Modified: 22nd December 2015

import random #imports random module.
play=input('Rock, Paper, Scissors, Lizard, Spock!\n')
opp=random.choice(['rock', 'paper', 'scissors', 'lizard', 'spock'])
print('Computer picked:', opp)
#player's choice and random computer choice, then prints.
if opp==play.lower(): print('Tie')
elif play=='Rock' or play=='rock':
    if opp=='scissors' or opp=='lizard': print('You Won')
    else: print('You Lose')
elif play=='Paper' or play=='paper':
    if opp=='rock' or opp=='spock': print('You Won')
    else: print('You Lose')
elif play=='Scissors' or play=='scissors':
    if opp=='paper' or opp=='lizard': print('You Won')
    else: print('You Lose')
elif play=='Spock' or play=='spock':
    if opp=='rock' or opp=='scissors': print('You Won')
    else: print('You Lose')
elif play=='Lizard' or play=='lizard':
    if opp=='paper' or opp=='spock': print('You Won')
    else: print('You Lose')
else: print('Invalid Option')
#if statements to control who wins.
