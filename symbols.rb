File.open("isyms.txt", 'w') do |file|
	i = -1;
	file.write "& #{i+=1}\n"
	file.write "^ #{i+=1}\n"
	"abcdefghijklmnopqrstuvwxyz".split('').each do |c|
		file.write "#{c} #{i+=1}\n"
	end
	"abcdefghijklmnopqrstuvwxyz".upcase().split('').each do |c|
		file.write "#{c} #{i+=1}\n"
	end
end

