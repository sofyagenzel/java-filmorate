# java-filmorate
![filmorate_db](https://github.com/sofyagenzel/java-filmorate/blob/main/filmorate_db.png)
Example of query:
get film by id=1:
select 
 * 
from film
where film_id=1;

get list of friend by id=1:
select 
  username
from user
join (select * from friend where user_id=1) as p on user.user_id=p.friend_id;

