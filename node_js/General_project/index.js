const { EventEmitter } =  require('events')
const eventEmitter = new EventEmitter();

eventEmitter.on('lunch',()=>{
    console.log('yum')
})

eventEmitter.emit('lunch')
eventEmitter.emit('lunch')




// Sync -> blocking

const{readFile , readFileSync} = require('fs')
const txt = readFileSync('./hello.txt','utf-8')
console.log(txt)

console.log('do this')



// Async 

readFile('./hello.txt','utf-8',(err,txt)=>{
    console.log(txt)
})
console.log('do this')





// // promises

// const { readFile} = require('fs').promises

// async function hello(){
//     const file = await readFile('./hello.txt','utf-8')
// }

// hello()




const express = require('express')
const app = express()
app.get('/',(request,response)=>{

    readFile('./home.html','utf-8',(err,html)=>{

        response.send(html)
    })

})

app.listen(process.env.PORT || 3000 ,() => console.log("Server online at port : 3000"))
