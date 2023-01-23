#!/bin/sh

SESSION="StockPortfolio"

SESSIONEXISTS=$(tmux list-sessions | grep $SESSION)

# Only create tmux session if it doesn't already exist
if [ "$SESSIONEXISTS" = "" ]
then
    
    tmux new-session -d -s $SESSION
    
    window=0
    tmux rename-window "Overview"
    tmux send-keys "cd stock-portfolio-frontend;npm run dev" C-m
    tmux split-window -v  -c "stock-portfolio-backend"
    tmux send-keys -t 1 "source venv/bin/activate" C-m
    tmux send-keys -t 1 "python src/main.py" C-m
    tmux split-window  -v -c "stock-portfolio-backend" "./start_mongo_server.sh"
    tmux select-layout even-vertical
    
    window=1
    tmux new-window -t $SESSION:$window -n "frontend"
    tmux send-keys -t "frontend" "cd stock-portfolio-frontend;code ." C-m
    
    window=2
    tmux new-window -t $SESSION:$window -n "backend"
    tmux send-keys -t "backend" "cd stock-portfolio-backend;code .;source venv/bin/activate" C-m
    
fi

tmux send-keys "tmux source-file ~/.tmux.config" C-m
tmux attach-session -t $SESSION:0
