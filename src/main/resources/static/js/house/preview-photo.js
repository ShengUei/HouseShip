//photo preview, covertToBase64
function convertFile(file) {
    return new Promise((resolve,reject)=>{
        // 建立FileReader物件
        let reader = new FileReader()
        // 註冊onload事件，取得result則resolve (會是一個Base64字串)
        reader.onload = () => { resolve(reader.result) }
        // 註冊onerror事件，若發生error則reject
        reader.onerror = () => { reject(reader.error) }
        // 讀取檔案
        reader.readAsDataURL(file)
    })
}

const carouselInner = document.querySelector('.carousel-inner');
function showPreviewImage(src, index) {
    let div = document.createElement('div');

    if(index == 0) {
        div.className = 'carousel-item active';
    } else {
        div.className = 'carousel-item';
    }

    let img = document.createElement('img');
    img.src = src;
    img.className = 'd-block w-100';
    img.alt = '找不到圖片';
    div.appendChild(img);
    carouselInner.appendChild(div);
}

function previewPhoto (event) {
    let fileArray = event.target.files;
    const carouselExampleInterval = document.getElementById('carouselExampleInterval');
    const carouselInner = document.querySelector('.carousel-inner');
    if(fileArray.length == 0) {
        carouselExampleInterval.style.display = 'none'
        carouselInner.innerHTML = '';
    } else {
        carouselInner.innerHTML = '';
    }

    const carouselControlPrev = document.getElementById('carousel-control-prev');
    const carouselControlNext = document.getElementById('carousel-control-next');

    if(fileArray.length == 1) {
        carouselControlPrev.style.display = 'none';
        carouselControlNext.style.display = 'none';
    } else {
        carouselControlPrev.style.display = 'flex';
        carouselControlNext.style.display = 'flex';
    }

    if (fileArray.length > 5) {
        alert("最多只能上傳5張照片");
        event.target.value = "";
        carouselExampleInterval.style.display = 'none'
        carouselInner.innerHTML = '';
        carouselControlPrev.style.display = 'none';
        carouselControlNext.style.display = 'none';
    }

    for (let i = 0; i < fileArray.length; i++) {
        if(fileArray[i].size <= 500000) {
            let base64 = convertFile(fileArray[i]);
            let lastPhoto = false
            base64.then(src => {
                if (i == fileArray.length - 1) {
                    lastPhoto = true
                }
                showPreviewImage(src, i);
            })
        } else {
            alert("照片大小必須小於5MB");
            event.target.value = "";
            carouselExampleInterval.style.display = 'none'
            carouselInner.innerHTML = '';
            carouselControlPrev.style.display = 'none';
            carouselControlNext.style.display = 'none';
        }
    }
    if (fileArray.length != 0) {
        carouselExampleInterval.style.display = 'block'
    }
}

document.getElementById('rest-btn').onclick = () => {
    const carouselExampleInterval = document.getElementById('carouselExampleInterval');
    const carouselInner = document.querySelector('.carousel-inner');
    carouselExampleInterval.style.display = 'none'
    carouselInner.innerHTML = '';
}
