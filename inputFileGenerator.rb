arg = ARGV[0]
File.open("FSTinput.txt", 'w') do |file|
	i = 0
	arg.split('').each do |c|
		file.write"#{i} #{i+=1} #{c}\n"
	end
	file.write "#{i}\n"
end